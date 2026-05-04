import React from 'react';
import { useNavigate, useLocation } from 'react-router-dom';
import { useMsal } from '@azure/msal-react';
import { 
  Box, Drawer, AppBar, Toolbar, List, Typography, Divider, 
  ListItem, ListItemButton, ListItemIcon, ListItemText, Button 
} from '@mui/material';

// Importation des icônes MUI
import DashboardIcon from '@mui/icons-material/Dashboard';
import PeopleIcon from '@mui/icons-material/People';
import BusinessIcon from '@mui/icons-material/Business';
import WorkIcon from '@mui/icons-material/Work';
import AccessTimeIcon from '@mui/icons-material/AccessTime';
import LogoutIcon from '@mui/icons-material/Logout';

const drawerWidth = 240;

function Layout({ children }) {
  const { instance } = useMsal();
  const navigate = useNavigate();
  const location = useLocation(); // Pour savoir sur quelle page on est

  const handleLogout = () => {
    instance.logoutRedirect({ postLogoutRedirectUri: "/" });
  };

  // Définition de notre menu
  const menuItems = [
    { text: 'Dashboard', icon: <DashboardIcon />, path: '/' },
    { text: 'Customers', icon: <BusinessIcon />, path: '/customers' },
    { text: 'Consultants', icon: <PeopleIcon />, path: '/consultants' },
    { text: 'Projekte', icon: <WorkIcon />, path: '/projects' },
    { text: 'Zeiteinträge', icon: <AccessTimeIcon />, path: '/entries' },
  ];

  return (
    <Box sx={{ display: 'flex' }}>
      {/* Barre de navigation en haut */}
      <AppBar position="fixed" sx={{ width: `calc(100% - ${drawerWidth}px)`, ml: `${drawerWidth}px`, backgroundColor: '#1976d2' }}>
        <Toolbar>
          <Typography variant="h6" noWrap component="div" sx={{ flexGrow: 1, fontWeight: 'bold' }}>
            SERP Admin Dashboard
          </Typography>
          <Button color="inherit" onClick={handleLogout} startIcon={<LogoutIcon />}>
            Abmelden
          </Button>
        </Toolbar>
      </AppBar>

      {/* Barre latérale (Sidebar) */}
      <Drawer
        sx={{
          width: drawerWidth,
          flexShrink: 0,
          '& .MuiDrawer-paper': { width: drawerWidth, boxSizing: 'border-box', backgroundColor: '#2c3e50', color: 'white' },
        }}
        variant="permanent"
        anchor="left"
      >
        <Toolbar>
          <Typography variant="h6" sx={{ fontWeight: 'bold', color: 'white', textAlign: 'center', width: '100%' }}>
            SERP Menü
          </Typography>
        </Toolbar>
        <Divider sx={{ backgroundColor: 'rgba(255,255,255,0.1)' }} />
        <List>
          {menuItems.map((item) => (
            <ListItem key={item.text} disablePadding>
              <ListItemButton 
                onClick={() => navigate(item.path)}
                // Met en surbrillance l'élément du menu si on est sur la bonne page
                selected={location.pathname === item.path}
                // --- MODIFICATION ICI ---
                sx={{ 
                  // Couleur de fond lors de la sélection (le bleu de l'AppBar)
                  '&.Mui-selected': { 
                    backgroundColor: '#1976d2' 
                  }, 
                  // Garder le bleu même au survol quand sélectionné
                  '&.Mui-selected:hover': { 
                    backgroundColor: '#1976d2',
                    opacity: 0.9 // Optionnel: légère variation au survol
                  },
                  // Survol simple (quand non sélectionné)
                  '&:hover': { 
                    backgroundColor: 'rgba(255,255,255,0.1)' 
                  } 
                }}
                // -----------------------
              >
                <ListItemIcon sx={{ color: 'white' }}>{item.icon}</ListItemIcon>
                <ListItemText primary={item.text} />
              </ListItemButton>
            </ListItem>
          ))}
        </List>
      </Drawer>

      {/* Contenu principal (là où les pages vont s'afficher) */}
      <Box component="main" sx={{ flexGrow: 1, bgcolor: '#f5f5f5', p: 3, minHeight: '100vh' }}>
        <Toolbar /> {/* Espace vide pour ne pas être caché par l'AppBar */}
        {children}
      </Box>
    </Box>
  );
}

export default Layout;