import { Component, Input, OnInit, ViewChild } from '@angular/core';
import { FormBuilder, FormControl, FormGroup, Validators } from '@angular/forms';
import { MatPaginator, MatSnackBar, MatSort, MatTableDataSource } from '@angular/material';
import { ActivatedRoute } from '@angular/router';

import { CommentService } from '../shared/comment.service';
import { AuthService } from './../../core/auth/auth.service';
import { Comment } from './../shared/comment.model';

@Component({
  selector: 'it-display-comment',
  templateUrl: './display-comment.component.html',
  styleUrls: ['./display-comment.component.css']
})
export class DisplayCommentComponent implements OnInit {
  displayedColumns = ['userUsername', 'content'];
  dataSource: MatTableDataSource<Comment>;
  comments: Comment[];
  issueId: number;
  pullRequestId: number;
  form: FormGroup;
  control: FormControl = new FormControl();

  @Input('type') type: string;

  @ViewChild(MatPaginator) paginator: MatPaginator;
  @ViewChild(MatSort) sort: MatSort;

  constructor(
    private commentService: CommentService,
    private route: ActivatedRoute,
    private formBuilder: FormBuilder,
    private snackBar: MatSnackBar,
    private authService: AuthService
  ) {}

  ngOnInit() {
    const fc = new FormControl();
    this.form = this.formBuilder.group({
      content: ['', Validators.required]
    });

    this.issueId = +this.route.snapshot.paramMap.get('issueId');
    this.pullRequestId = +this.route.snapshot.paramMap.get('pullRequestId');
    let id;
    if (this.type === 'issue') {
      id = this.issueId;
    } else if (this.type === 'pull-request') {
      id = this.pullRequestId;
    }
    this.commentService.getById(id, this.type).subscribe(comments => {
      this.comments = comments;
      this.dataSource = new MatTableDataSource(this.comments);
      this.dataSource.paginator = this.paginator;
      this.dataSource.sort = this.sort;
    });
  }

  get content() {
    return this.form.get('content');
  }

  onCreateComment() {
    if (this.form.valid) {
      const comment = new Comment();
      const user = this.authService.user;
      (comment.userId = user.id), (comment.userUsername = user.username);
      if (this.type === 'issue') {
        comment.issueId = this.issueId;
      } else if (this.type === 'pull-request') {
        comment.pullRequestId = this.pullRequestId;
      }
      comment.content = this.content.value;

      this.commentService.createComment(comment).subscribe(
        createdComment => {
          this.comments.push(createdComment);
          this.dataSource = new MatTableDataSource(this.comments);
          this.dataSource.paginator = this.paginator;
          this.dataSource.sort = this.sort;
          this.form.reset({ content: '' });
          while (this.dataSource.paginator.hasNextPage()) {
            this.dataSource.paginator.nextPage();
          }
          this.snackBar.open('You have successfully created a comment.', 'OK', {
            duration: 2000
          });
        },
        err => {
          this.snackBar.open(err.message, 'Cancel', {
            duration: 2000
          });
        }
      );
    }
  }
}
