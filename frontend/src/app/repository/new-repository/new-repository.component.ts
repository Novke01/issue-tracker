import { Component, Inject, OnInit } from '@angular/core';
import { FormBuilder, FormControl, FormGroup, Validators } from '@angular/forms';
import { MAT_DIALOG_DATA, MatDialogRef, MatSnackBar } from '@angular/material';

import { User } from '../../core/auth/user.model';
import { UserService } from '../../user/shared/user.service';
import { RepositorySave } from '../shared/repository-save.model';
import { AuthService } from '../../core/auth/auth.service';
import { RepositoryService } from '../shared/repository.service';

@Component({
  selector: 'it-new-repository',
  templateUrl: './new-repository.component.html',
  styleUrls: ['./new-repository.component.css']
})
export class NewRepositoryComponent implements OnInit {
  form: FormGroup;
  users: User[];
  control: FormControl = new FormControl();
  repository: RepositorySave = new RepositorySave();

  constructor(
    private formBuilder: FormBuilder,
    private dialogRef: MatDialogRef<NewRepositoryComponent>,
    private userService: UserService,
    private repositoryService: RepositoryService,
    private authService: AuthService,
    private snackBar: MatSnackBar,
    @Inject(MAT_DIALOG_DATA) public data: any
  ) {}

  ngOnInit() {
    this.form = this.formBuilder.group({
      name: ['', Validators.required],
      url: ['', Validators.required],
      description: ['', Validators.required]
    });
    this.userService.getAll().subscribe(data => {
      this.users = data.filter(user => user.id !== this.authService.user.id);
    });
  }

  submit() {
    if (this.form.valid) {
      this.repository.name = this.form.value.name;
      this.repository.url = this.form.value.url;
      this.repository.description = this.form.value.description;
      this.repository.ownerId = this.authService.user.id;
      this.repository.contributors = this.control.value.map(
        contributor => contributor.id
      );

      this.repositoryService.saveRepository(this.repository).subscribe(
        repository => {
          this.dialogRef.close(repository);
        },
        err => {
          this.snackBar.open(err.message, 'Cancel', {
            duration: 2000
          });
        }
      );
    }
  }

  cancelDialog() {
    this.dialogRef.close(null);
  }

  get name() {
    return this.form.get('name');
  }
  get url() {
    return this.form.get('url');
  }
  get description() {
    return this.form.get('description');
  }
}
