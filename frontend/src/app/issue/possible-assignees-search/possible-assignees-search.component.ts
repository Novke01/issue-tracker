import { Component, EventEmitter, Input, OnInit, Output } from '@angular/core';
import { Observable } from 'rxjs/Observable';
import { debounceTime, distinctUntilChanged, switchMap } from 'rxjs/operators';
import { Subject } from 'rxjs/Subject';

import { User } from '../../core/auth/user.model';
import { RepositoryService } from '../../repository/shared/repository.service';

@Component({
  selector: 'it-possible-assignees-search',
  templateUrl: './possible-assignees-search.component.html',
  styleUrls: ['./possible-assignees-search.component.css']
})
export class PossibleAssigneesSearchComponent implements OnInit {
  @Input() repositoryId: number;
  @Input() assignees: User[] = [];
  @Output() userAssigned: EventEmitter<User[]> = new EventEmitter<User[]>();

  possibleAssignees$: Observable<User[]>;
  private searchTerms = new Subject<string>();
  searchBoxValue: string;

  constructor(private repositoryService: RepositoryService) {}

  // Push a search term into the observable stream.
  search(): void {
    this.searchTerms.next(this.searchBoxValue);
  }

  ngOnInit(): void {
    this.searchBoxValue = '';
    this.possibleAssignees$ = this.searchTerms.pipe(
      // wait 300ms after each keystroke before considering the term
      debounceTime(300),

      // ignore new term if same as previous term
      distinctUntilChanged(),

      // switch to new search observable each time the term changes
      switchMap((term: string) =>
        this.repositoryService.searchOwnerAndContributors(
          this.repositoryId,
          term
        )
      )
    );
  }

  isAssigned(userId: number): boolean {
    return this.assignees.some(function(a) {
      return a.id === userId;
    });
  }

  assignUser(user: User): void {
    if (!this.isAssigned(user.id)) {
      this.assignees.push(user);
      this.userAssigned.emit(this.assignees);
      this.searchBoxValue = '';
    }
  }
}
