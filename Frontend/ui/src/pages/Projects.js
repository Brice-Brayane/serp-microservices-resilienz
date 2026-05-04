import React, { useState, useEffect } from 'react';
import { useMsal } from '@azure/msal-react';
import { loginRequest } from '../authConfig';
import {
  Typography, Paper, Table, TableBody, TableCell,
  TableContainer, TableHead, TableRow, CircularProgress, Alert, Box,
  Button, TextField, Dialog, DialogActions, DialogContent, DialogTitle,
  IconButton, Tooltip, MenuItem, Select, InputLabel, FormControl, Chip, OutlinedInput
} from '@mui/material';
import EditIcon from '@mui/icons-material/Edit';
import DeleteIcon from '@mui/icons-material/Delete';

function Projects() {
  const { instance, accounts } = useMsal();

  const [projects, setProjects] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);

  const [availableCustomers, setAvailableCustomers] = useState([]);
  const [availableConsultants, setAvailableConsultants] = useState([]);

  const [openDialog, setOpenDialog] = useState(false);
  const [currentProject, setCurrentProject] = useState({
    id: null, name: "", start: "", end: "", status: "running", customerId: "", consultantIds: [] 
  });
  const [saving, setSaving] = useState(false);

  const gatewayUrl = "http://localhost:8080/api";

  // --- STYLE PERSONNALISÉ POUR L'EN-TÊTE ---
  const headerStyle = {
    backgroundColor: '#2c3e50',
    '& .MuiTableCell-head': {
      color: 'white',
      fontWeight: 'bold',
    },
  };

  const fetchData = async () => {
    setLoading(true);
    try {
      const response = await instance.acquireTokenSilent({
        ...loginRequest, account: accounts[0]
      });

      const headers = { 'Authorization': `Bearer ${response.accessToken}`, 'Content-Type': 'application/json' };

      const [projectsRes, customersRes, consultantsRes] = await Promise.all([
        fetch(`${gatewayUrl}/projects`, { headers }),
        fetch(`${gatewayUrl}/customers`, { headers }),
        fetch(`${gatewayUrl}/consultants`, { headers })
      ]);

      if (!projectsRes.ok) throw new Error(`Projekte API Fehler: ${projectsRes.status}`);

      setProjects(await projectsRes.json());
      setAvailableCustomers(customersRes.ok ? await customersRes.json() : []);
      setAvailableConsultants(consultantsRes.ok ? await consultantsRes.json() : []);

    } catch (err) {
      console.error("Fehler beim Laden der Daten:", err);
      setError(err.message);
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => {
    fetchData();
  }, [instance, accounts]);

  const getCustomerName = (id) => {
    if (!id) return "-";
    const customer = availableCustomers.find(c => c.id === id || c.customerId === id);
    return customer ? customer.name : `Unbekannt (ID: ${id})`;
  };

  const getConsultantName = (id) => {
    if (!id) return "-";
    const consultant = availableConsultants.find(c => c.id === id || c.consultantId === id);
    return consultant ? consultant.name : `Unbekannt (ID: ${id})`;
  };

  const handleSave = async () => {
    if (!currentProject.name.trim() || !currentProject.customerId) {
      alert("Bitte füllen Sie mindestens den Namen und den Kunden aus.");
      return;
    }
    setSaving(true);
    try {
      const response = await instance.acquireTokenSilent({ ...loginRequest, account: accounts[0] });
      const isUpdating = currentProject.id !== null;
      const url = isUpdating ? `${gatewayUrl}/projects/${currentProject.id}` : `${gatewayUrl}/projects`;
      const payload = {
        id: currentProject.id,
        name: currentProject.name,
        start: currentProject.start ? new Date(currentProject.start).toISOString() : null,
        end: currentProject.end ? new Date(currentProject.end).toISOString() : null,
        status: currentProject.status,
        customer: { customerId: currentProject.customerId },
        projectStaff: currentProject.consultantIds.map(id => ({ consultantId: id }))
      };
      const apiResponse = await fetch(url, {
        method: isUpdating ? 'PUT' : 'POST',
        headers: { 'Authorization': `Bearer ${response.accessToken}`, 'Content-Type': 'application/json' },
        body: JSON.stringify(payload)
      });
      if (!apiResponse.ok) throw new Error(`HTTP Fehler: ${apiResponse.status}`);
      fetchData(); 
      setOpenDialog(false);
    } catch (err) {
      alert("Speichern fehlgeschlagen: " + err.message);
    } finally {
      setSaving(false);
    }
  };

  const openSaveDialog = (project = null) => {
    if (project) {
      setCurrentProject({
        id: project.id || project.projectId,
        name: project.name,
        start: project.start ? new Date(project.start).toISOString().slice(0, 10) : "",
        end: project.end ? new Date(project.end).toISOString().slice(0, 10) : "",
        status: project.status || "running",
        customerId: project.customer?.customerId || "",
        consultantIds: project.projectStaff ? project.projectStaff.map(c => c.consultantId) : []
      });
    } else {
      setCurrentProject({ id: null, name: "", start: "", end: "", status: "running", customerId: "", consultantIds: [] });
    }
    setOpenDialog(true);
  };

  const handleDelete = async (idToDelete) => {
    if (!window.confirm("Sind Sie sicher, dass Sie dieses Projekt löschen möchten?")) return;
    try {
      const response = await instance.acquireTokenSilent({ ...loginRequest, account: accounts[0] });
      const apiResponse = await fetch(`${gatewayUrl}/projects/${idToDelete}`, {
        method: 'DELETE',
        headers: { 'Authorization': `Bearer ${response.accessToken}` }
      });
      if (!apiResponse.ok) throw new Error(`HTTP Fehler: ${apiResponse.status}`);
      setProjects(projects.filter(p => (p.id || p.projectId) !== idToDelete));
    } catch (err) {
      alert("Löschen fehlgeschlagen: " + err.message);
    }
  };

  return (
    <Box>
      <Typography variant="h4" color="primary" gutterBottom sx={{ fontWeight: 'bold' }}>
        Projekte Übersicht (Projects)
      </Typography>

      <Box sx={{ display: 'flex', justifyContent: 'flex-start', mb: 2, mt: 3 }}>
        <Button variant="contained" color="primary" onClick={() => openSaveDialog()}>+ Neues Projekt</Button>
      </Box>

      {loading && <CircularProgress sx={{ mt: 4 }} />}
      {error && <Alert severity="error" sx={{ mt: 2 }}>Fehler beim Laden der Daten: {error}.</Alert>}

      {!loading && !error && (
        <TableContainer component={Paper} sx={{ boxShadow: 0, border: '1px solid #e0e0e0' }}>
          <Table aria-label="projects table">
            {/* --- MODIFICATION ICI : Style de l'en-tête principal --- */}
            <TableHead sx={headerStyle}>
              <TableRow>
                <TableCell>ID</TableCell>
                <TableCell>Name</TableCell>
                <TableCell>Start</TableCell>
                <TableCell>Ende</TableCell>
                <TableCell>Status</TableCell>
                <TableCell>Kunde (Customer)</TableCell>
                <TableCell>Berater (Project Staff)</TableCell>
                <TableCell align="right">Aktionen</TableCell>
              </TableRow>
            </TableHead>

            <TableBody>
              {projects.length === 0 ? (
                <TableRow><TableCell colSpan={8} align="center" sx={{ py: 3 }}>Keine Projekte gefunden.</TableCell></TableRow>
              ) : (
                projects.map((project) => (
                  <TableRow key={project.id || project.projectId} sx={{ '& > *': { borderBottom: '1px solid #e0e0e0' } }}>
                    <TableCell sx={{ verticalAlign: 'top', pt: 3 }}>{project.id || project.projectId}</TableCell>
                    <TableCell sx={{ verticalAlign: 'top', pt: 3 }}>{project.name}</TableCell>
                    <TableCell sx={{ verticalAlign: 'top', pt: 3 }}>{project.start ? new Date(project.start).toLocaleDateString('de-DE') : '-'}</TableCell>
                    <TableCell sx={{ verticalAlign: 'top', pt: 3 }}>{project.end ? new Date(project.end).toLocaleDateString('de-DE') : '-'}</TableCell>
                    <TableCell sx={{ verticalAlign: 'top', pt: 3 }}>{project.status || '-'}</TableCell>

                    <TableCell sx={{ padding: 1, verticalAlign: 'top' }}>
                      {project.customer ? (
                        <Table size="small">
                          {/* Optionnel: En-tête de sous-table en gris foncé léger */}
                          <TableHead sx={{ backgroundColor: '#f8f9fa' }}>
                            <TableRow>
                              <TableCell sx={{ fontWeight: 'bold', borderBottom: '1px solid #ddd', px: 1 }}>ID</TableCell>
                              <TableCell sx={{ fontWeight: 'bold', borderBottom: '1px solid #ddd', px: 1 }}>Name</TableCell>
                            </TableRow>
                          </TableHead>
                          <TableBody>
                            <TableRow>
                              <TableCell sx={{ borderBottom: 'none', px: 1 }}>{project.customer.customerId}</TableCell>
                              <TableCell sx={{ borderBottom: 'none', px: 1 }}>{getCustomerName(project.customer.customerId)}</TableCell>
                            </TableRow>
                          </TableBody>
                        </Table>
                      ) : (
                        <Typography variant="body2" sx={{ color: '#999', fontStyle: 'italic', px: 1 }}>Ohne Kunde</Typography>
                      )}
                    </TableCell>

                    <TableCell sx={{ padding: 1, verticalAlign: 'top' }}>
                      {project.projectStaff && project.projectStaff.length > 0 ? (
                        <Table size="small">
                          <TableHead sx={{ backgroundColor: '#f8f9fa' }}>
                            <TableRow>
                              <TableCell sx={{ fontWeight: 'bold', borderBottom: '1px solid #ddd', px: 1 }}>ID</TableCell>
                              <TableCell sx={{ fontWeight: 'bold', borderBottom: '1px solid #ddd', px: 1 }}>Name</TableCell>
                            </TableRow>
                          </TableHead>
                          <TableBody>
                            {project.projectStaff.map((staff, index) => (
                              <TableRow key={index}>
                                <TableCell sx={{ borderBottom: 'none', px: 1 }}>{staff.consultantId}</TableCell>
                                <TableCell sx={{ borderBottom: 'none', px: 1 }}>{getConsultantName(staff.consultantId)}</TableCell>
                              </TableRow>
                            ))}
                          </TableBody>
                        </Table>
                      ) : (
                        <Typography variant="body2" sx={{ color: '#999', fontStyle: 'italic', px: 1 }}>Kein Personal</Typography>
                      )}
                    </TableCell>

                    <TableCell align="right" sx={{ verticalAlign: 'top', pt: 2 }}>
                      <Tooltip title="Bearbeiten">
                        <IconButton size="small" onClick={() => openSaveDialog(project)}>
                          <EditIcon fontSize="small" sx={{ color: '#888' }} />
                        </IconButton>
                      </Tooltip>
                      <Tooltip title="Löschen">
                        <IconButton size="small" onClick={() => handleDelete(project.id || project.projectId)}>
                          <DeleteIcon fontSize="small" sx={{ color: '#888' }} />
                        </IconButton>
                      </Tooltip>
                    </TableCell>
                  </TableRow>
                ))
              )}
            </TableBody>
          </Table>
        </TableContainer>
      )}

      {/* MODALE - Inchangée */}
      <Dialog open={openDialog} onClose={() => setOpenDialog(false)} maxWidth="sm" fullWidth>
        <DialogTitle>{currentProject.id ? "Projekt bearbeiten" : "Neues Projekt anlegen"}</DialogTitle>
        <DialogContent>
          <TextField margin="dense" label="Projektname" fullWidth variant="outlined" value={currentProject.name} onChange={(e) => setCurrentProject({ ...currentProject, name: e.target.value })} sx={{ mt: 1 }} />
          <Box sx={{ display: 'flex', gap: 2, mt: 2 }}>
            <TextField label="Startdatum" type="date" fullWidth InputLabelProps={{ shrink: true }} value={currentProject.start} onChange={(e) => setCurrentProject({ ...currentProject, start: e.target.value })} />
            <TextField label="Enddatum" type="date" fullWidth InputLabelProps={{ shrink: true }} value={currentProject.end} onChange={(e) => setCurrentProject({ ...currentProject, end: e.target.value })} />
          </Box>
          <TextField select margin="dense" label="Status" fullWidth value={currentProject.status} onChange={(e) => setCurrentProject({ ...currentProject, status: e.target.value })} sx={{ mt: 2 }}>
            <MenuItem value="running">running</MenuItem>
            <MenuItem value="completed">completed</MenuItem>
            <MenuItem value="planned">planned</MenuItem>
          </TextField>
          
          <FormControl fullWidth sx={{ mt: 2 }}>
            <InputLabel>Kunde (Customer)</InputLabel>
            <Select value={currentProject.customerId} onChange={(e) => setCurrentProject({ ...currentProject, customerId: e.target.value })} input={<OutlinedInput label="Kunde (Customer)" />}>
              {availableCustomers.map((c) => (
                <MenuItem key={c.id || c.customerId} value={c.id || c.customerId}>{c.name}</MenuItem>
              ))}
            </Select>
          </FormControl>

          <FormControl fullWidth sx={{ mt: 2 }}>
            <InputLabel>Berater (Project Staff)</InputLabel>
            <Select multiple value={currentProject.consultantIds} onChange={(e) => setCurrentProject({ ...currentProject, consultantIds: e.target.value })} input={<OutlinedInput label="Berater (Project Staff)" />}
              renderValue={(selected) => (
                <Box sx={{ display: 'flex', flexWrap: 'wrap', gap: 0.5 }}>
                  {selected.map((value) => <Chip key={value} label={getConsultantName(value)} />)}
                </Box>
              )}
            >
              {availableConsultants.map((c) => (
                <MenuItem key={c.id || c.consultantId} value={c.id || c.consultantId}>{c.name}</MenuItem>
              ))}
            </Select>
          </FormControl>
        </DialogContent>
        <DialogActions>
          <Button onClick={() => setOpenDialog(false)} color="secondary">Abbrechen</Button>
          <Button onClick={handleSave} color="primary" variant="contained" disabled={saving}>
            {saving ? <CircularProgress size={24} /> : "Speichern"}
          </Button>
        </DialogActions>
      </Dialog>
    </Box>
  );
}

export default Projects;