import { TestBed, inject } from '@angular/core/testing';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { SharedModule } from '../../shared/shared.module';
import { LabelService } from './label.service';

describe('LabelService', () => {
  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, SharedModule],
      providers: [LabelService]
    });
  });

  it(
    'should be created',
    inject([LabelService], (service: LabelService) => {
      expect(service).toBeTruthy();
    })
  );
});
