import React from 'react';
import { BrowserRouter, Routes, Route } from 'react-router-dom';
import { useIsAuthenticated, useMsal } from '@azure/msal-react';
import { loginRequest } from './authConfig';
import { Box, Typography, Button, Container, Paper } from '@mui/material';
import LoginIcon from '@mui/icons-material/Login';
import Consultants from './pages/Consultants';
import Customers from './pages/Customers';
import Entries from './pages/Entries'; 
import Projects from './pages/Projects';

// Importation de nos nouveaux composants de structure
import Layout from './components/Layout';
import Dashboard from './pages/Dashboard';

function App() {
  const { instance } = useMsal();
  const isAuthenticated = useIsAuthenticated();

  // Fonction pour déclencher la connexion (avec redirection)
  const handleLogin = () => {
    instance.loginRedirect(loginRequest).catch(e => {
      console.error("Anmeldefehler:", e); 
    });
  };

  // 1️⃣ Si l'utilisateur N'EST PAS connecté : On affiche un bel écran de login centré
  if (!isAuthenticated) {
    return (
      <Box sx={{ display: 'flex', justifyContent: 'center', alignItems: 'center', minHeight: '100vh', backgroundColor: '#f5f5f5' }}>
        <Container maxWidth="sm">
          <Paper elevation={3} sx={{ p: 5, textAlign: 'center', borderRadius: 2 }}>
            <Typography variant="h4" color="primary" gutterBottom sx={{ fontWeight: 'bold' }}>
              SERP Admin Dashboard
            </Typography>
            <Typography variant="body1" color="text.secondary" sx={{ mb: 4 }}>
              Bitte authentifizieren Sie sich mit Ihrem Microsoft-Konto, um Zugriff auf die Unternehmensdaten zu erhalten.
            </Typography>
            <Button variant="contained" size="large" onClick={handleLogin} startIcon={<LoginIcon />}>
              Azure Anmeldung
            </Button>
          </Paper>
        </Container>
      </Box>
    );
  }

  // 2️⃣ Si l'utilisateur EST connecté : On affiche l'application avec la Sidebar (Layout) et le Router
  return (
    <BrowserRouter>
      {/* Le composant Layout contient la barre de gauche et la barre du haut */}
      <Layout>
        {/* Ici, on définit les différentes pages de l'application */}
        <Routes>
          {/* Page par défaut */}
          <Route path="/" element={<Dashboard />} />
          
          {/* Les futures pages pour tes microservices (pour l'instant, c'est juste du texte) */}
          <Route path="/consultants" element={<Consultants />} />
          <Route path="/customers" element={<Customers />} />
          <Route path="/entries" element={<Entries />} />
          <Route path="/projects" element={<Projects />} />
          
        </Routes>
      </Layout>
    </BrowserRouter>
  );
}

export default App;