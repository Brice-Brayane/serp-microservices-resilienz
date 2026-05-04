export const msalConfig = {
    auth: {
        clientId: "a0cb0ac9-caa5-4518-bd7d-cc7748f853e4", // Anwendungs-ID
        authority: "https://login.microsoftonline.com/90866267-22b3-468c-a24d-0686aa19596f", // Ton Mandant-ID
        redirectUri: "http://localhost:3000",
    },
    cache: {
        cacheLocation: "sessionStorage",
        storeAuthStateInCookie: false,
    }
};

export const loginRequest = {
    scopes: ["a0cb0ac9-caa5-4518-bd7d-cc7748f853e4/.default"]
};