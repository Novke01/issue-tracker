import { CommonModule } from '@angular/common';
import { HttpClientModule } from '@angular/common/http';
import { NgModule } from '@angular/core';

import { AppRoutingModule } from '../app-routing.module';
import { SharedModule } from '../shared/shared.module';
import { DisplayCommentComponent } from './display-comment/display-comment.component';
import { CommentService } from './shared/comment.service';

@NgModule({
  imports: [CommonModule, SharedModule, HttpClientModule, AppRoutingModule],
  exports: [DisplayCommentComponent],
  declarations: [DisplayCommentComponent],
  providers: [CommentService]
})
export class CommentModule {}
