import { Component, input, inject, OnInit, HostBinding, signal } from '@angular/core';
import { CommonModule } from '@angular/common';
import { MatIconModule } from '@angular/material/icon';
import { MatButtonModule } from '@angular/material/button';
import { RouterLink, RouterLinkActive } from '@angular/router';
import { MatDialog, MatDialogModule } from '@angular/material/dialog';
import { CategoryService } from '../../../../core/services/category';
import { Category } from '../../../../shared/models/task.model';
import { CategoryFormComponent } from '../../../../features/categories/components/category-form/category-form';

@Component({
  selector: 'app-sidebar',
  standalone: true,
  imports: [CommonModule, MatIconModule, MatButtonModule, RouterLink, RouterLinkActive, MatDialogModule],
  templateUrl: './sidebar.html',
  styleUrl: './sidebar.css',
})
export class SidebarComponent implements OnInit {
  private categoryService = inject(CategoryService);
  private dialog = inject(MatDialog);
  
  isCollapsed = input(false);
  // Agora apenas expomos o sinal do serviço
  categories = this.categoryService.categories;

  @HostBinding('class.collapsed') get collapsed() {
    return this.isCollapsed();
  }

  ngOnInit(): void {
    // Carrega apenas se a lista estiver vazia para evitar chamadas duplicadas no bootstrap
    if (this.categories().length === 0) {
      this.categoryService.refresh();
    }
  }

  openNewCategoryForm(): void {
    const dialogRef = this.dialog.open(CategoryFormComponent, {
      width: '400px'
    });

    // O refresh já é feito pelo tap() no CategoryService após o sucesso
  }
}
