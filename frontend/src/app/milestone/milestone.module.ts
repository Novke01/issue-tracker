import { CommonModule, DatePipe } from '@angular/common';
import { HttpClientModule } from '@angular/common/http';
import { NgModule } from '@angular/core';

import { AppRoutingModule } from '../app-routing.module';
import { SharedModule } from '../shared/shared.module';
import { CreateMilestoneComponent } from './create-milestone/create-milestone.component';
import { RepositoryMilestonesComponent } from './repository-milestones/repository-milestones.component';
import { MilestoneService } from './shared/milestone.service';

@NgModule({
  imports: [CommonModule, SharedModule, HttpClientModule, AppRoutingModule],
  exports: [RepositoryMilestonesComponent],
  declarations: [RepositoryMilestonesComponent, CreateMilestoneComponent],
  providers: [MilestoneService, DatePipe]
})
export class MilestoneModule {}
