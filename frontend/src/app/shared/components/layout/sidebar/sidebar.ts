import { Component, input } from '@angular/core';
import { CommonModule } from '@angular/common';
import { MatIconModule } from '@angular/material/icon';
import { MatButtonModule } from '@angular/material/button';

@Component({
  selector: 'app-sidebar',
  standalone: true,
  imports: [CommonModule, MatIconModule, MatButtonModule],
  templateUrl: './sidebar.html',
  styleUrl: './sidebar.css',
})
export class SidebarComponent {
  isCollapsed = input(false);

  categories = [
    { name: 'Trabalho', icon: 'work', active: true },
    { name: 'Pessoal', icon: 'person', active: false },
    { name: 'Estudos', icon: 'school', active: false },
  ];
}
