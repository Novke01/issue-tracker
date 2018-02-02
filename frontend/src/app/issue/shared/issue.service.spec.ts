import { HttpClientTestingModule } from '@angular/common/http/testing';
import { inject, TestBed } from '@angular/core/testing';

import { SharedModule } from '../../shared/shared.module';
import { IssueService } from './issue.service';

describe("IssueService", () => {
  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, SharedModule],
      providers: [IssueService]
    });
  });

  it(
    "should be created",
    inject([IssueService], (service: IssueService) => {
      expect(service).toBeTruthy();
    })
  );
});
