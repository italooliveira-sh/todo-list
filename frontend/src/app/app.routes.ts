import { Routes } from '@angular/router';
import { LoginComponent } from './features/auth/login/login';

export const routes: Routes = [
    { path: 'login', component: LoginComponent },
    { path: 'register', component: LoginComponent }, // Temporário, até criar o RegisterComponent
    { path: 'dashboard', component: LoginComponent }, // Temporário, até criar o DashboardComponent
    { path: '', redirectTo: '/login', pathMatch: 'full' }
];
