import { TestBed, inject } from '@angular/core/testing';

import { IssueService } from './issue.service';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { SharedModule } from '../../shared/shared.module';

describe('IssueService', () => {
  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [
        HttpClientTestingModule,
        SharedModule
      ],
      providers: [IssueService]
    });
  });

  it('should be created', inject([IssueService], (service: IssueService) => {
    expect(service).toBeTruthy();
  }));
});
