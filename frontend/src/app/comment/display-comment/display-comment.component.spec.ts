import { HttpClientTestingModule } from '@angular/common/http/testing';
import { async, ComponentFixture, TestBed } from '@angular/core/testing';
import { ReactiveFormsModule } from '@angular/forms';
import { RouterTestingModule } from '@angular/router/testing';
import { of } from 'rxjs/observable/of';

import { AuthService } from '../../core/auth/auth.service';
import { SharedModule } from '../../shared/shared.module';
import { Comment } from './../shared/comment.model';
import { CommentService } from './../shared/comment.service';
import { DisplayCommentComponent } from './display-comment.component';

describe('DisplayCommentComponent', () => {
  let component: DisplayCommentComponent;
  let fixture: ComponentFixture<DisplayCommentComponent>;
  let commentService: CommentService;
  let authService: AuthService;

  const comment: Comment = {
    id: 1,
    content: 'title',
    userId: 1,
    userUsername: 'username',
    issueId: 1,
    pullRequestId: 1
  };
  const comments = [comment];

  beforeEach(
    async(() => {
      TestBed.configureTestingModule({
        imports: [
          ReactiveFormsModule,
          HttpClientTestingModule,
          RouterTestingModule.withRoutes([]),
          SharedModule
        ],
        providers: [CommentService, AuthService],
        declarations: [DisplayCommentComponent]
      }).compileComponents();
    })
  );

  beforeEach(() => {
    fixture = TestBed.createComponent(DisplayCommentComponent);
    component = fixture.componentInstance;
    commentService = fixture.debugElement.injector.get(CommentService);
    authService = fixture.debugElement.injector.get(AuthService);

    spyOn(commentService, 'getById').and.returnValue(of(comments));

    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it(
    'should be able to get all comments',
    async(() => {
      component.ngOnInit();
      fixture.detectChanges();
      fixture.whenStable().then(() => {
        expect(commentService.getById).toHaveBeenCalled();
        fixture.detectChanges();
        fixture.whenStable().then(() => {
          expect(component.comments).toBe(comments);
        });
      });
    })
  );
});
