import { Component, inject, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { MatCardModule } from '@angular/material/card';
import { MatButtonModule } from '@angular/material/button';
import { MatIconModule } from '@angular/material/icon';
import { MatChipsModule } from '@angular/material/chips';
import { MatInputModule } from '@angular/material/input';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatBadgeModule } from '@angular/material/badge';
import { TokenService } from '../../core/services/token';
import { Router } from '@angular/router';

@Component({
  selector: 'app-dashboard',
  standalone: true,
  imports: [
    CommonModule,
    MatCardModule,
    MatButtonModule,
    MatIconModule,
    MatChipsModule,
    MatInputModule,
    MatFormFieldModule,
    MatBadgeModule
  ],
  templateUrl: './dashboard.html',
  styleUrl: './dashboard.css'
})
export class DashboardComponent implements OnInit {
  private tokenService = inject(TokenService);
  private router = inject(Router);

  userName: string = 'Italo Oliveira';

  ngOnInit(): void {
    // Por enquanto, usamos o nome estático conforme a imagem
    // No futuro, buscaremos o nome do usuário logado
  }

  logout(): void {
    this.tokenService.removeToken();
    this.router.navigate(['/login']);
  }
}
