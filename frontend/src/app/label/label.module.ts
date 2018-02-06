import { CommonModule } from '@angular/common';
import { HttpClientModule } from '@angular/common/http';
import { CUSTOM_ELEMENTS_SCHEMA, NgModule } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { CreateLabelComponent } from './create-label/create-label.component';
import { RepositoryLabelsComponent } from './repository-labels/repository-labels.component';
import { SharedModule } from '../shared/shared.module';
import { AppRoutingModule } from '../app-routing.module';
import { LabelService } from './shared/label.service';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { NoopAnimationsModule } from '@angular/platform-browser/animations';

@NgModule({
  imports: [
    CommonModule,
    SharedModule,
    HttpClientModule,
    AppRoutingModule,
    FormsModule,
    BrowserAnimationsModule,
    NoopAnimationsModule
  ],
  exports: [RepositoryLabelsComponent, CreateLabelComponent],
  declarations: [CreateLabelComponent, RepositoryLabelsComponent],
  providers: [LabelService],
  schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class LabelModule {}
