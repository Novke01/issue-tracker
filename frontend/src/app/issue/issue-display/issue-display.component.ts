import { Component, OnInit } from '@angular/core';
import { IssueService } from '../shared/issue.service';
import { Issue } from '../shared/issue.model';
import { Router, ActivatedRoute, ParamMap } from '@angular/router';
import { User } from '../../core/auth/user.model';
import { MatSnackBar } from '@angular/material';
import { FormGroup, AbstractControl, FormBuilder, Validators, FormControl } from '@angular/forms';

@Component({
  selector: 'it-issue-display',
  templateUrl: './issue-display.component.html',
  styleUrls: ['./issue-display.component.css']
})
export class IssueDisplayComponent implements OnInit {

  issue: Issue = new Issue();
  form: FormGroup;
  control: FormControl = new FormControl();
  repositoryId: number;
  assignees: User[];

  constructor(
    private router: Router,
    private route: ActivatedRoute,
    private formBuilder: FormBuilder,
    private issueService: IssueService,
    private snackBar: MatSnackBar,
  ){
  }

  ngOnInit() {
    this.repositoryId = +this.route.snapshot.paramMap.get('repoId');
    this.route.paramMap
    .switchMap((params: ParamMap) =>
      this.issueService.getIssueById(+params.get('issueId'))).subscribe(issue => {
        this.issue = issue;
        this.issueService.getAssigneesByIssueId(this.issue.id).subscribe(assignees => {
          this.assignees = assignees;
          
        });
      });

      let fc = new FormControl();
      this.form = this.formBuilder.group({
        title: ['', Validators.required],
        description: ['']
      });
  }

  get title() { return this.form.get('title'); }
  get description() { return this.form.get('description'); }

  onUserAssigned(assignees){
    var user = assignees[assignees.length - 1]
    this.issueService.insertAssignee(this.issue.id, user.id).subscribe(_ => {
      this.assignees = assignees;
    });
  }

  unassignUser(user){
    this.issueService.removeAssignee(this.issue.id, user.id).subscribe(_ => {
      this.assignees = this.assignees.filter(function( a ) {
        return a.id !== user.id;
      });
    });
  }

  onUpdateIssue() {
    if (this.form.valid) {
      this.issue.title = this.title.value;
      this.issue.description = this.description.value;
      this.issueService.updateIssue(this.issue).subscribe(
        issue => {
          console.log(issue)
          this.snackBar.open('You have successfully updated an issue.', 'OK', {
            duration: 2000
          })
        },
        err => {
          console.log(err);
          this.snackBar.open(err.message, 'Cancel', {
            duration: 2000
          });
        }
      );
    }
  }

}
