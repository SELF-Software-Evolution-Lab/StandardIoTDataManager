import { Routes } from '@angular/router';

import { activateRoute, passwordRoute, settingsRoute } from './';

const ACCOUNT_ROUTES = [activateRoute, passwordRoute, settingsRoute];

export const accountState: Routes = [
    {
        path: '',
        children: ACCOUNT_ROUTES
    }
];
