import React, { useState, useEffect } from 'react';
import { useMsal } from '@azure/msal-react';
import { loginRequest } from '../authConfig';
import { 
  Typography, Paper, Table, TableBody, TableCell, 
  TableContainer, TableHead, TableRow, CircularProgress, Alert, Box,
  Button, TextField, Dialog, DialogActions, DialogContent, DialogTitle,
  IconButton, Tooltip
} from '@mui/material';
import EditIcon from '@mui/icons-material/Edit';
import DeleteIcon from '@mui/icons-material/Delete';

function Consultants() {
  const { instance, accounts } = useMsal();
  
  // États pour les données
  const [consultants, setConsultants] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);

  // États pour le formulaire (Création/Édition)
  const [openDialog, setOpenDialog] = useState(false);
  const [currentConsultant, setCurrentConsultant] = useState({ id: null, name: "" });
  const [saving, setSaving] = useState(false);

  // URL de la Gateway pointant vers le consultant-service
  const apiUrl = "http://localhost:8080/api/consultants"; 

  // ==========================================
  // READ : Récupérer les consultants (GET)
  // ==========================================
  const fetchConsultants = async () => {
    setLoading(true);
    try {
      const response = await instance.acquireTokenSilent({
        ...loginRequest,
        account: accounts[0]
      });

      const apiResponse = await fetch(apiUrl, {
        method: 'GET',
        headers: {
          'Authorization': `Bearer ${response.accessToken}`,
          'Content-Type': 'application/json'
        }
      });

      if (!apiResponse.ok) throw new Error(`Erreur HTTP: ${apiResponse.status}`);
      
      const data = await apiResponse.json();
      setConsultants(data); 

    } catch (err) {
      console.error("Erreur FETCH:", err);
      setError(err.message);
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => {
    fetchConsultants();
  }, [instance, accounts]);

  // ==========================================
  // CREATE / UPDATE : Sauvegarder (POST / PUT)
  // ==========================================
  const handleSave = async () => {
    if (!currentConsultant.name.trim()) return;
    setSaving(true);

    try {
      const response = await instance.acquireTokenSilent({
        ...loginRequest,
        account: accounts[0]
      });

      const isUpdating = currentConsultant.id !== null;
      const url = isUpdating ? `${apiUrl}/${currentConsultant.id}` : apiUrl;
      const method = isUpdating ? 'PUT' : 'POST';

      const payload = {
        id: currentConsultant.id,
        name: currentConsultant.name
      };

      const apiResponse = await fetch(url, {
        method: method,
        headers: {
          'Authorization': `Bearer ${response.accessToken}`,
          'Content-Type': 'application/json'
        },
        body: JSON.stringify(payload)
      });

      if (!apiResponse.ok) throw new Error(`Erreur HTTP: ${apiResponse.status}`);
      
      fetchConsultants();
      setOpenDialog(false);
      setCurrentConsultant({ id: null, name: "" });

    } catch (err) {
      console.error("Erreur SAVE:", err);
      alert("Erreur lors de la sauvegarde : " + err.message);
    } finally {
      setSaving(false);
    }
  };

  const openSaveDialog = (consultant = { id: null, name: "" }) => {
    setCurrentConsultant(consultant);
    setOpenDialog(true);
  };

  // ==========================================
  // DELETE : Supprimer (DELETE)
  // ==========================================
  const handleDelete = async (idToDelete) => {
    if (!window.confirm("Bist du sicher, dass du diesen Berater löschen möchtest?")) return;

    try {
      const response = await instance.acquireTokenSilent({
        ...loginRequest,
        account: accounts[0]
      });

      const apiResponse = await fetch(`${apiUrl}/${idToDelete}`, {
        method: 'DELETE',
        headers: {
          'Authorization': `Bearer ${response.accessToken}`
        }
      });

      if (!apiResponse.ok) throw new Error(`Erreur HTTP: ${apiResponse.status}`);
      
      setConsultants(consultants.filter(c => c.id !== idToDelete));

    } catch (err) {
      console.error("Erreur DELETE:", err);
      alert("Erreur lors de la suppression : " + err.message);
    }
  };

  return (
    <Box>
      <Typography variant="h4" color="primary" gutterBottom sx={{ fontWeight: 'bold' }}>
        Berater Übersicht (Consultants)
      </Typography>

      <Box sx={{ display: 'flex', justifyContent: 'flex-start', mb: 2, mt: 3 }}>
        <Button variant="contained" color="primary" onClick={() => openSaveDialog()}>
          + New Consultant
        </Button>
      </Box>

      {loading && <CircularProgress sx={{ mt: 4 }} />}

      {error && (
        <Alert severity="error" sx={{ mt: 2 }}>
          Fehler beim Laden der Daten: {error}.
        </Alert>
      )}

      {!loading && !error && (
        <TableContainer component={Paper} sx={{ boxShadow: 3 }}>
          <Table sx={{ minWidth: 650 }}>
            <TableHead sx={{ backgroundColor: '#2c3e50' }}>
              <TableRow>
                <TableCell sx={{ color: 'white', fontWeight: 'bold' }}>ID</TableCell>
                <TableCell sx={{ color: 'white', fontWeight: 'bold' }}>Name</TableCell>
                <TableCell sx={{ color: 'white', fontWeight: 'bold' }}>Projekte (Assigned Projects)</TableCell>
                <TableCell sx={{ color: 'white', fontWeight: 'bold', textAlign: 'right' }}>Aktionen</TableCell>
              </TableRow>
            </TableHead>
            <TableBody>
              {consultants.length === 0 ? (
                <TableRow>
                  <TableCell colSpan={4} align="center" sx={{ py: 3 }}>
                    Keine Berater gefunden.
                  </TableCell>
                </TableRow>
              ) : (
                consultants.map((consultant) => (
                  <TableRow key={consultant.id}>
                    <TableCell>{consultant.id}</TableCell>
                    <TableCell>{consultant.name}</TableCell>
                    <TableCell>
                      {consultant.bookedProjects && consultant.bookedProjects.length > 0 
                        ? (
                          <Box>
                            {consultant.bookedProjects.map(p => (
                              <Typography key={p.id} variant="body2">• {p.name} (ID: {p.projectID})</Typography>
                            ))}
                          </Box>
                        )
                        : <Typography variant="body2" color="textSecondary">Keine Zuordnungen</Typography>
                      }
                    </TableCell>
                    <TableCell align="right">
                      <Tooltip title="Bearbeiten">
                        <IconButton color="primary" onClick={() => openSaveDialog(consultant)}>
                          <EditIcon />
                        </IconButton>
                      </Tooltip>
                      <Tooltip title="Löschen">
                        <IconButton color="error" onClick={() => handleDelete(consultant.id)}>
                          <DeleteIcon />
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

      <Dialog open={openDialog} onClose={() => setOpenDialog(false)}>
        <DialogTitle>{currentConsultant.id ? "Berater bearbeiten" : "Neuen Berater anlegen"}</DialogTitle>
        <DialogContent>
          <TextField
            autoFocus margin="dense" label="Name des Beraters" fullWidth variant="outlined"
            value={currentConsultant.name}
            onChange={(e) => setCurrentConsultant({ ...currentConsultant, name: e.target.value })}
            disabled={saving}
          />
        </DialogContent>
        <DialogActions>
          <Button onClick={() => setOpenDialog(false)} color="secondary" disabled={saving}>
            Abbrechen
          </Button>
          <Button onClick={handleSave} color="primary" variant="contained" disabled={saving || !currentConsultant.name.trim()}>
            {saving ? <CircularProgress size={24} /> : "Speichern"}
          </Button>
        </DialogActions>
      </Dialog>
    </Box>
  );
}

export default Consultants;