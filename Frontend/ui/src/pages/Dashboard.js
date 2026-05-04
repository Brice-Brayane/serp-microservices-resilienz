import React from 'react';
import { Box, Typography, Paper } from '@mui/material';

function Dashboard() {
  return (
    <Paper elevation={3} sx={{ p: 4, textAlign: 'center', borderRadius: 2 }}>
      <Typography variant="h4" color="primary" gutterBottom>
        Willkommen im SERP-System! 
      </Typography>
      <Typography variant="body1" color="text.secondary">
        Sie haben sich erfolgreich über Microsoft Entra ID angemeldet. 
        Wählen Sie einen Bereich aus dem Menü auf der linken Seite.
      </Typography>
    </Paper>
  );
}

export default Dashboard;