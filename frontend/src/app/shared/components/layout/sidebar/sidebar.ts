import { Component, input, inject, OnInit, HostBinding, ChangeDetectorRef } from '@angular/core';
import { CommonModule } from '@angular/common';
import { MatIconModule } from '@angular/material/icon';
import { MatButtonModule } from '@angular/material/button';
import { RouterLink, RouterLinkActive } from '@angular/router';
import { CategoryService } from '../../../../core/services/category';
import { Category } from '../../../../shared/models/task.model';

@Component({
  selector: 'app-sidebar',
  standalone: true,
  imports: [CommonModule, MatIconModule, MatButtonModule, RouterLink, RouterLinkActive],
  templateUrl: './sidebar.html',
  styleUrl: './sidebar.css',
})
export class SidebarComponent implements OnInit {
  private categoryService = inject(CategoryService);
  private cdr = inject(ChangeDetectorRef);
  
  isCollapsed = input(false);
  categories: Category[] = [];

  @HostBinding('class.collapsed') get collapsed() {
    return this.isCollapsed();
  }

  ngOnInit(): void {
    this.loadCategories();
  }

  loadCategories(): void {
    this.categoryService.findAll().subscribe({
      next: (res) => {
        this.categories = res;
        this.cdr.detectChanges(); // Força o Angular a reconhecer a mudança de [] para a lista real
      },
      error: (err) => console.error('Erro ao carregar categorias na sidebar', err)
    });
  }
}