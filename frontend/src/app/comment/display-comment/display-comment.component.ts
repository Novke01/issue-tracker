import { Component, ElementRef, Input, OnInit, ViewChild } from '@angular/core';
import { FormBuilder, FormControl, FormGroup, Validators } from '@angular/forms';
import { MatPaginator, MatSnackBar, MatSort, MatTableDataSource } from '@angular/material';
import { ActivatedRoute } from '@angular/router';

import { User } from '../../core/auth/user.model';
import { Issue } from '../../issue/shared/issue.model';
import { PullRequestService } from '../../pull-request/shared/pull-request.service';
import { CommentService } from '../shared/comment.service';
import { AuthService } from './../../core/auth/auth.service';
import { PullRequest } from './../../pull-request/shared/pull-request.model';
import { RepositoryService } from './../../repository/shared/repository.service';
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
  mode: MODE;
  contributors: User[] = [];
  issues: Issue[];
  pullRequests: PullRequest[];
  items: string[] = [];
  lastChar: string;
  htmlMap: Map<string, string> = new Map<string, string>();

  @Input('type') type: string;

  @ViewChild(MatPaginator) paginator: MatPaginator;
  @ViewChild(MatSort) sort: MatSort;
  @ViewChild('comment') inputComment: ElementRef;

  constructor(
    private commentService: CommentService,
    private route: ActivatedRoute,
    private formBuilder: FormBuilder,
    private snackBar: MatSnackBar,
    private authService: AuthService,
    private repositoryService: RepositoryService,
    private pullRequestService: PullRequestService
  ) {}

  ngOnInit() {
    const fc = new FormControl();
    this.form = this.formBuilder.group({
      content: ['', Validators.required]
    });

    this.issueId = +this.route.snapshot.paramMap.get('issueId');
    this.pullRequestId = +this.route.snapshot.paramMap.get('pullRequestId');
    const repoId = this.route.snapshot.paramMap.get('repoId');
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

    this.repositoryService
      .getContributorsByRepositoryId(repoId)
      .subscribe(users => {
        this.contributors = this.contributors.concat(users);
      });

    this.repositoryService.getOwnerByRepositoryId(repoId).subscribe(owner => {
      this.contributors.unshift(owner);
    });

    this.repositoryService
      .getIssuesByRepositoryId(+repoId)
      .subscribe(issues => {
        this.issues = issues;
      });

    this.pullRequestService
      .getByRepositoryId(+repoId)
      .subscribe(pullRequests => {
        this.pullRequests = pullRequests;
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
      this.htmlMap.forEach((value: string, key: string) => {
        comment.content = comment.content.replace(key, value);
      });

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

  onKey(event: any) {
    const lastKey = event.key;
    if (
      (this.lastChar === '@' ||
        this.lastChar === '#' ||
        this.lastChar === '&') &&
      lastKey === 'Backspace'
    ) {
      this.items = [];
    }
    this.lastChar = this.content.value.slice(-1);
    if (lastKey === '@') {
      this.items = [];
      this.mode = MODE.USERS;
      this.contributors.forEach(user => {
        this.items.push('@' + user.id + ' ' + user.username);
      });
    } else if (lastKey === '#') {
      this.items = [];
      this.mode = MODE.ISSUES;
      this.issues.forEach(issue => {
        this.items.push('#' + issue.id + ' ' + issue.title);
      });
    } else if (lastKey === '&') {
      this.items = [];
      this.mode = MODE.PRS;
      this.pullRequests.forEach(pr => {
        this.items.push('&' + pr.id + ' ' + pr.title);
      });
    }
  }

  clickOnItem(item: string) {
    const value = this.content.value;
    this.items = [];
    if (this.mode === MODE.USERS) {
      const indexLast = value.lastIndexOf('@');
      const tag = item;
      this.content.setValue(value.substring(0, indexLast) + tag);
      this.mode = MODE.EMPTY;
    } else if (this.mode === MODE.ISSUES) {
      const indexLast = value.lastIndexOf('#');
      const tag = item;
      this.content.setValue(value.substring(0, indexLast) + tag);
      const id = +this.content.value.substring(indexLast).match(/\d+/)[0];
      const repoId = this.issues
        .filter(issue => issue.id === id)
        .map(issue => issue.repositoryId);
      // tslint:disable-next-line:max-line-length
      this.htmlMap.set(
        tag,
        '<a target="_blank" href="/repositories/' +
          repoId +
          '/issues/' +
          id +
          '">' +
          tag +
          '</a>'
      );
      this.mode = MODE.EMPTY;
    } else if (this.mode === MODE.PRS) {
      const indexLast = value.lastIndexOf('&');
      const tag = item;
      this.content.setValue(value.substring(0, indexLast) + tag);
      const id = +this.content.value.substring(indexLast).match(/\d+/)[0];
      const repoId = this.pullRequests
        .filter(pr => pr.id === id)
        .map(pr => pr.repositoryId);
      // tslint:disable-next-line:max-line-length
      this.htmlMap.set(
        tag,
        '<a target="_blank" href="/repository/' +
          repoId +
          '/pull-request/' +
          id +
          '">' +
          tag +
          '</a>'
      );
      this.mode = MODE.EMPTY;
    }
    this.inputComment.nativeElement.focus();
  }
}

enum MODE {
  EMPTY = 1,
  USERS,
  ISSUES,
  PRS
}
