import React, { useState, useEffect } from 'react';
import { useMsal } from '@azure/msal-react';
import { loginRequest } from '../authConfig';
import { 
  Typography, Paper, Table, TableBody, TableCell, 
  TableContainer, TableHead, TableRow, CircularProgress, Alert, Box,
  Button, TextField, Dialog, DialogActions, DialogContent, DialogTitle,
  IconButton, Tooltip, MenuItem, Select, InputLabel, FormControl, OutlinedInput
} from '@mui/material';
import EditIcon from '@mui/icons-material/Edit';

function Entries() {
  const { instance, accounts } = useMsal();
  
  const [entries, setEntries] = useState([]);
  const [availableProjects, setAvailableProjects] = useState([]);
  const [availableConsultants, setAvailableConsultants] = useState([]);
  
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);

  const [openDialog, setOpenDialog] = useState(false);
  const [currentEntry, setCurrentEntry] = useState({ 
    entryId: null, date: "", hours: "", projectId: "", consultantId: "" 
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

      const [entriesRes, projectsRes, consultantsRes] = await Promise.all([
        fetch(`${gatewayUrl}/time/entries`, { headers }),
        fetch(`${gatewayUrl}/projects`, { headers }),
        fetch(`${gatewayUrl}/consultants`, { headers })
      ]);

      const entriesData = entriesRes.ok ? await entriesRes.json() : [];
      const projectsData = projectsRes.ok ? await projectsRes.json() : [];
      const consultantsData = consultantsRes.ok ? await consultantsRes.json() : [];

      setEntries(entriesData);
      setAvailableProjects(projectsData);
      setAvailableConsultants(consultantsData);

    } catch (err) {
      setError(err.message);
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => {
    fetchData();
  }, [instance, accounts]);

  const handleSave = async () => {
    if (!currentEntry.date || !currentEntry.hours || !currentEntry.projectId || !currentEntry.consultantId) {
      alert("Bitte füllen Sie alle Felder aus (inklusive Uhrzeit!).");
      return;
    }
    setSaving(true);

    try {
      const response = await instance.acquireTokenSilent({ ...loginRequest, account: accounts[0] });

      const isUpdating = currentEntry.entryId !== null;
      let url = `${gatewayUrl}/time/entries/${currentEntry.consultantId}`;
      if (isUpdating) url += `/${currentEntry.entryId}`;

      const payload = {
        entryId: currentEntry.entryId,
        date: new Date(currentEntry.date).toISOString(),
        hours: parseInt(currentEntry.hours, 10),
        project: { projectId: parseInt(currentEntry.projectId, 10) },
        consultant: { consultantId: parseInt(currentEntry.consultantId, 10) }
      };

      const apiResponse = await fetch(url, {
        method: isUpdating ? 'PUT' : 'POST',
        headers: { 'Authorization': `Bearer ${response.accessToken}`, 'Content-Type': 'application/json' },
        body: JSON.stringify(payload)
      });

      if (!apiResponse.ok) throw new Error(`HTTP Error: ${apiResponse.status}`);
      
      fetchData(); 
      setOpenDialog(false);

    } catch (err) {
      alert("Speichern fehlgeschlagen: " + err.message);
    } finally {
      setSaving(false);
    }
  };

  const openSaveDialog = (entry = null) => {
    if (entry) {
      setCurrentEntry({
        entryId: entry.entryId,
        date: entry.date ? new Date(entry.date).toISOString().slice(0, 16) : "",
        hours: entry.hours || "",
        projectId: entry.project?.projectId || "",
        consultantId: entry.consultant?.consultantId || ""
      });
    } else {
      setCurrentEntry({ entryId: null, date: "", hours: "", projectId: "", consultantId: "" });
    }
    setOpenDialog(true);
  };

  const getProjectName = (id) => availableProjects.find(p => p.id === id)?.name || `Unbekannt (ID: ${id})`;
  const getConsultantName = (id) => availableConsultants.find(c => c.id === id)?.name || `Unbekannt (ID: ${id})`;

  return (
    <Box>
      <Typography variant="h4" color="primary" gutterBottom sx={{ fontWeight: 'bold' }}>Zeiteinträge</Typography>
      
      <Box sx={{ display: 'flex', justifyContent: 'flex-start', mb: 2, mt: 3 }}>
        <Button variant="contained" color="primary" onClick={() => openSaveDialog()}>+ Neuer Eintrag</Button>
      </Box>

      {loading && <CircularProgress sx={{ mt: 4 }} />}
      {error && <Alert severity="error" sx={{ mt: 2 }}>{error}</Alert>}

      {!loading && !error && (
        <TableContainer component={Paper} sx={{ boxShadow: 0, border: '1px solid #e0e0e0' }}>
          <Table>
            {/* --- STYLE APPLIQUÉ ICI --- */}
            <TableHead sx={headerStyle}>
              <TableRow>
                <TableCell>ID</TableCell>
                <TableCell>Datum & Uhrzeit</TableCell>
                <TableCell>Stunden</TableCell>
                <TableCell>Berater</TableCell>
                <TableCell>Projekt</TableCell>
                <TableCell align="right">Aktionen</TableCell>
              </TableRow>
            </TableHead>
            <TableBody>
              {entries.length === 0 ? (
                <TableRow><TableCell colSpan={6} align="center" sx={{ py: 3 }}>Keine Zeiteinträge gefunden.</TableCell></TableRow>
              ) : entries.map((entry) => (
                <TableRow key={entry.entryId} sx={{ '&:last-child td, &:last-child th': { border: 0 } }}>
                  <TableCell>{entry.entryId}</TableCell>
                  <TableCell>{entry.date ? new Date(entry.date).toLocaleString('de-DE') : '-'}</TableCell>
                  <TableCell>{entry.hours} h</TableCell>
                  <TableCell>{getConsultantName(entry.consultant?.consultantId)}</TableCell>
                  <TableCell>{getProjectName(entry.project?.projectId)}</TableCell>
                  <TableCell align="right">
                    <Tooltip title="Bearbeiten">
                        <IconButton size="small" onClick={() => openSaveDialog(entry)}>
                            <EditIcon fontSize="small" sx={{ color: '#888' }} />
                        </IconButton>
                    </Tooltip>
                  </TableCell>
                </TableRow>
              ))}
            </TableBody>
          </Table>
        </TableContainer>
      )}

      {/* DIALOG DE CRÉATION */}
      <Dialog open={openDialog} onClose={() => setOpenDialog(false)} maxWidth="sm" fullWidth>
        <DialogTitle>{currentEntry.entryId ? "Zeiteintrag bearbeiten" : "Neuen Zeiteintrag erfassen"}</DialogTitle>
        <DialogContent>
          <FormControl fullWidth sx={{ mt: 1, mb: 2 }}>
            <InputLabel>Berater (Consultant)</InputLabel>
            <Select
              value={currentEntry.consultantId}
              onChange={(e) => setCurrentEntry({ ...currentEntry, consultantId: e.target.value })}
              input={<OutlinedInput label="Berater (Consultant)" />}
              disabled={currentEntry.entryId !== null}
            >
              {availableConsultants.map((c) => (
                <MenuItem key={c.id} value={c.id}>{c.name}</MenuItem>
              ))}
            </Select>
          </FormControl>

          <FormControl fullWidth sx={{ mb: 2 }}>
            <InputLabel>Projekt</InputLabel>
            <Select
              value={currentEntry.projectId}
              onChange={(e) => setCurrentEntry({ ...currentEntry, projectId: e.target.value })}
              input={<OutlinedInput label="Projekt" />}
            >
              {availableProjects.map((p) => (
                <MenuItem key={p.id} value={p.id}>{p.name}</MenuItem>
              ))}
            </Select>
          </FormControl>

          <TextField
            margin="dense"
            label="Datum und Uhrzeit"
            type="datetime-local"
            fullWidth
            InputLabelProps={{ shrink: true }}
            value={currentEntry.date}
            onChange={(e) => setCurrentEntry({ ...currentEntry, date: e.target.value })}
            sx={{ mb: 2 }}
          />

          <TextField
            margin="dense"
            label="Stundenanzahl"
            type="number"
            fullWidth
            InputProps={{ inputProps: { min: 1, max: 24 } }}
            value={currentEntry.hours}
            onChange={(e) => setCurrentEntry({ ...currentEntry, hours: e.target.value })}
          />

        </DialogContent>
        <DialogActions>
          <Button onClick={() => setOpenDialog(false)} color="secondary">Abbrechen</Button>
          <Button onClick={handleSave} variant="contained" color="primary" disabled={saving}>
            {saving ? <CircularProgress size={24} /> : "Speichern"}
          </Button>
        </DialogActions>
      </Dialog>
    </Box>
  );
}

export default Entries;