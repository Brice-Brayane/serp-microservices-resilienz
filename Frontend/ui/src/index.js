import React from 'react';
import ReactDOM from 'react-dom/client';
import './index.css';
import App from './App';
import { PublicClientApplication } from '@azure/msal-browser';
import { MsalProvider } from '@azure/msal-react';
import { msalConfig } from './authConfig';

const msalInstance = new PublicClientApplication(msalConfig);

// 1. On initialise MSAL
msalInstance.initialize().then(() => {
  
  // 2. LA PIÈCE MANQUANTE : On demande à MSAL de "lire" l'URL après une redirection
  msalInstance.handleRedirectPromise().then((response) => {
    
    // Si Microsoft a bien renvoyé un compte via l'URL
    if (response !== null && response.account !== null) {
      msalInstance.setActiveAccount(response.account);
    } else {
      // Sinon, on vérifie s'il y a déjà un compte sauvegardé dans le navigateur
      const currentAccounts = msalInstance.getAllAccounts();
      if (currentAccounts.length > 0) {
        msalInstance.setActiveAccount(currentAccounts[0]);
      }
    }
    
    // 3. On lance l'affichage de React SEULEMENT après avoir vérifié la sécurité
    const root = ReactDOM.createRoot(document.getElementById('root'));
    root.render(
      <MsalProvider instance={msalInstance}>
        <App />
      </MsalProvider>
    );

  }).catch(error => {
    console.error("Erreur de retour Microsoft :", error);
  });
});