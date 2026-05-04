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

function Customers() {
  const { instance, accounts } = useMsal();
  
  // States für die Datenanzeige
  const [customers, setCustomers] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);

  // States für das Formular (Anlegen/Bearbeiten)
  const [openDialog, setOpenDialog] = useState(false);
  const [currentCustomer, setCurrentCustomer] = useState({ customerId: null, name: "", city: "" });
  const [saving, setSaving] = useState(false);

  // Die URL führt über das API-Gateway zum Customer-Service
  const apiUrl = "http://localhost:8080/api/customers"; 

  // ==========================================
  // READ: Kunden vom Server laden (GET)
  // ==========================================
  const fetchCustomers = async () => {
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

      if (!apiResponse.ok) throw new Error(`Server-Fehler: ${apiResponse.status}`);
      
      const data = await apiResponse.json();
      setCustomers(data); 

    } catch (err) {
      console.error("Fehler beim Laden:", err);
      setError(err.message);
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => {
    fetchCustomers();
  }, [instance, accounts]);

  // ==========================================
  // CREATE / UPDATE: Speichern (POST / PUT)
  // ==========================================
  const handleSave = async () => {
    if (!currentCustomer.name.trim()) return;
    setSaving(true);

    try {
      const response = await instance.acquireTokenSilent({
        ...loginRequest,
        account: accounts[0]
      });

      // Wenn eine customerId existiert, nutzen wir PUT zum Update, sonst POST zum Neuanlegen
      const isUpdating = currentCustomer.customerId !== null;
      const url = isUpdating ? `${apiUrl}/${currentCustomer.customerId}` : apiUrl;
      const method = isUpdating ? 'PUT' : 'POST';

      const apiResponse = await fetch(url, {
        method: method,
        headers: {
          'Authorization': `Bearer ${response.accessToken}`,
          'Content-Type': 'application/json'
        },
        body: JSON.stringify(currentCustomer) // Sendet customerId, name und city
      });

      if (!apiResponse.ok) throw new Error(`Server-Fehler: ${apiResponse.status}`);
      
      fetchCustomers(); // Liste nach Erfolg aktualisieren
      setOpenDialog(false);
      setCurrentCustomer({ customerId: null, name: "", city: "" });

    } catch (err) {
      console.error("Fehler beim Speichern:", err);
      alert("Fehler beim Speichern des Kunden: " + err.message);
    } finally {
      setSaving(false);
    }
  };

  // Hilfsfunktion zum Öffnen des Dialogs (für Neu oder Bearbeiten)
  const openSaveDialog = (customer = { customerId: null, name: "", city: "" }) => {
    setCurrentCustomer(customer);
    setOpenDialog(true);
  };

  // ==========================================
  // DELETE: Kunden löschen (DELETE)
  // ==========================================
  const handleDelete = async (customerId) => {
    if (!window.confirm("Möchten Sie diesen Kunden wirklich löschen?")) return;

    try {
      const response = await instance.acquireTokenSilent({
        ...loginRequest,
        account: accounts[0]
      });

      const apiResponse = await fetch(`${apiUrl}/${customerId}`, {
        method: 'DELETE',
        headers: {
          'Authorization': `Bearer ${response.accessToken}`
        }
      });

      if (!apiResponse.ok) throw new Error(`Server-Fehler: ${apiResponse.status}`);
      
      // Filtern der Liste im Frontend für sofortiges Feedback
      setCustomers(customers.filter(c => c.customerId !== customerId));

    } catch (err) {
      console.error("Fehler beim Löschen:", err);
      alert("Löschen fehlgeschlagen: " + err.message);
    }
  };

  return (
    <Box>
      <Typography variant="h4" color="primary" gutterBottom sx={{ fontWeight: 'bold' }}>
        Kunden Übersicht (Customers)
      </Typography>

      <Box sx={{ display: 'flex', justifyContent: 'flex-start', mb: 2, mt: 3 }}>
        <Button variant="contained" color="primary" onClick={() => openSaveDialog()}>
          + Neuer Kunde
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
                <TableCell sx={{ color: 'white', fontWeight: 'bold' }}>Customer ID</TableCell>
                <TableCell sx={{ color: 'white', fontWeight: 'bold' }}>Name</TableCell>
                <TableCell sx={{ color: 'white', fontWeight: 'bold' }}>Stadt (City)</TableCell>
                <TableCell sx={{ color: 'white', fontWeight: 'bold', textAlign: 'right' }}>Aktionen</TableCell>
              </TableRow>
            </TableHead>
            <TableBody>
              {customers.length === 0 ? (
                <TableRow>
                  <TableCell colSpan={4} align="center" sx={{ py: 3 }}>
                    Keine Kunden gefunden.
                  </TableCell>
                </TableRow>
              ) : (
                customers.map((customer) => (
                  <TableRow key={customer.customerId}>
                    <TableCell>{customer.customerId}</TableCell>
                    <TableCell>{customer.name}</TableCell>
                    <TableCell>{customer.city || '-'}</TableCell>
                    <TableCell align="right">
                      <Tooltip title="Bearbeiten">
                        <IconButton color="primary" onClick={() => openSaveDialog(customer)}>
                          <EditIcon />
                        </IconButton>
                      </Tooltip>
                      <Tooltip title="Löschen">
                        <IconButton color="error" onClick={() => handleDelete(customer.customerId)}>
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

      {/* Dialog für CRUD-Operationen */}
      <Dialog open={openDialog} onClose={() => setOpenDialog(false)}>
        <DialogTitle>{currentCustomer.customerId ? "Kunden bearbeiten" : "Neuen Kunden anlegen"}</DialogTitle>
        <DialogContent>
          <TextField
            autoFocus margin="dense" label="Name des Kunden" fullWidth variant="outlined"
            value={currentCustomer.name}
            onChange={(e) => setCurrentCustomer({ ...currentCustomer, name: e.target.value })}
            disabled={saving}
          />
          <TextField
            margin="dense" label="Stadt (City)" fullWidth variant="outlined"
            value={currentCustomer.city}
            onChange={(e) => setCurrentCustomer({ ...currentCustomer, city: e.target.value })}
            disabled={saving}
            sx={{ mt: 2 }}
          />
        </DialogContent>
        <DialogActions>
          <Button onClick={() => setOpenDialog(false)} color="secondary" disabled={saving}>
            Abbrechen
          </Button>
          <Button onClick={handleSave} color="primary" variant="contained" disabled={saving || !currentCustomer.name.trim()}>
            {saving ? <CircularProgress size={24} /> : "Speichern"}
          </Button>
        </DialogActions>
      </Dialog>
    </Box>
  );
}

export default Customers;