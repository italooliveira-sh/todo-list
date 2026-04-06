import { Component, inject, Output, EventEmitter, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { MatIconModule } from '@angular/material/icon';
import { MatButtonModule } from '@angular/material/button';
import { TokenService } from '../../../../core/services/token';
import { Router } from '@angular/router';

@Component({
  selector: 'app-header',
  standalone: true,
  imports: [CommonModule, MatIconModule, MatButtonModule],
  templateUrl: './header.html',
  styleUrl: './header.css',
})
export class HeaderComponent implements OnInit {
  private tokenService = inject(TokenService);
  private router = inject(Router);

  @Output() toggleMenu = new EventEmitter<void>();

  userName = '';
  userInitials = '';

  ngOnInit(): void {
    this.loadUserData();
  }

  loadUserData(): void {
    // O serviço já devolve o nome formatado em Title Case
    this.userName = this.tokenService.getUserName();
    const names = this.userName.trim().split(/\s+/);
    
    if (names.length >= 2) {
      this.userInitials = (names[0][0] + names[1][0]).toUpperCase();
    } else if (names.length === 1 && names[0].length >= 2) {
      this.userInitials = names[0].substring(0, 2).toUpperCase();
    } else if (names.length === 1) {
      this.userInitials = names[0].toUpperCase();
    } else {
      this.userInitials = '??';
    }
  }

  logout(): void {
    this.tokenService.removeToken();
    this.router.navigate(['/login']);
  }

  goToDashboard(): void {
    this.router.navigate(['/dashboard']);
  }

  onToggleMenu(): void {
    this.toggleMenu.emit();
  }
}
