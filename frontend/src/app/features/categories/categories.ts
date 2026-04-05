import { Component, inject, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { MatTableModule } from '@angular/material/table';
import { MatButtonModule } from '@angular/material/button';
import { MatIconModule } from '@angular/material/icon';
import { MatDialog, MatDialogModule } from '@angular/material/dialog';
import { MatSnackBar, MatSnackBarModule } from '@angular/material/snack-bar';
import { MatTooltipModule } from '@angular/material/tooltip';
import { CategoryService } from '../../core/services/category';
import { Category } from '../../shared/models/task.model';
import { CategoryFormComponent } from './components/category-form/category-form';

@Component({
  selector: 'app-categories',
  standalone: true,
  imports: [
    CommonModule, 
    MatTableModule, 
    MatButtonModule, 
    MatIconModule, 
    MatDialogModule,
    MatSnackBarModule,
    MatTooltipModule
  ],
  templateUrl: './categories.html',
  styleUrl: './categories.css'
})
export class CategoryListComponent implements OnInit {
  private categoryService = inject(CategoryService);
  private dialog = inject(MatDialog);
  private snackBar = inject(MatSnackBar);

  // Consome o sinal global do serviço
  categories = this.categoryService.categories;
  displayedColumns: string[] = ['color', 'name', 'taskCount', 'actions'];

  ngOnInit(): void {
    this.categoryService.refresh();
  }

  openForm(category?: Category): void {
    this.dialog.open(CategoryFormComponent, {
      width: '400px',
      data: category
    });
    // Não precisamos mais do afterClosed() para dar refresh, 
    // pois o CategoryService já faz o refresh no tap()
  }

  deleteCategory(category: Category): void {
    if (confirm(`Tem certeza que deseja excluir a categoria "${category.name}"? As tarefas associadas ficarão sem categoria.`)) {
      this.categoryService.delete(category.id).subscribe({
        next: () => this.snackBar.open('Categoria excluída com sucesso', 'Sucesso', { duration: 3000 }),
        error: () => this.snackBar.open('Erro ao excluir categoria', 'Fechar', { duration: 3000 })
      });
    }
  }
}
