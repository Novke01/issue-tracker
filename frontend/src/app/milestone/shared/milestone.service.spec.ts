import { HttpRequest } from '@angular/common/http';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import { async, TestBed } from '@angular/core/testing';

import { environment } from '../../../environments/environment';
import { Milestone } from './milestone.model';
import { MilestoneService } from './milestone.service';

describe('MilestoneService', () => {
  let service: MilestoneService;
  let httpMock: HttpTestingController;
  const milestoneUrl = 'api/milestones';

  beforeEach(
    async(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        providers: [MilestoneService]
      });
      service = TestBed.get(MilestoneService);
      httpMock = TestBed.get(HttpTestingController);
    })
  );

  it('should create', () => {
    expect(service).toBeTruthy();
  });

  it('should be able to get milestones by repository id', () => {
    const repositoryId = 1;
    const milestone = new Milestone();

    milestone.id = 1;
    milestone.title = 'milestone title';
    milestone.dueDate = '15/09/2018';
    milestone.description = 'description';
    milestone.repositoryId = repositoryId;

    const responseMilestones = [milestone];

    service.getByRepositoryId(repositoryId).subscribe(milestones => {
      expect(milestones).toEqual(responseMilestones);
    });

    const url = `${
      environment.baseUrl
    }${milestoneUrl}/repository/${repositoryId}`;

    httpMock
      .expectOne((req: HttpRequest<any>) => {
        return req.url === url && req.method === 'GET';
      }, `GET to api/milestones/repository/${repositoryId}`)
      .flush(responseMilestones, { status: 200, statusText: 'OK' });

    httpMock.verify();
  });

  it('should be able to save new milestone', () => {
    const dummyMilestone: Milestone = {
      id: 1,
      title: 'milestone title',
      description: 'milestone description',
      dueDate: '15/09/2018',
      repositoryId: 1
    };

    const responseMilestone: Milestone = {
      id: dummyMilestone.id,
      title: dummyMilestone.title,
      description: dummyMilestone.description,
      dueDate: dummyMilestone.dueDate,
      repositoryId: dummyMilestone.repositoryId
    };

    service.createMilestone(dummyMilestone).subscribe(milestone => {
      expect(milestone).toBe(responseMilestone);
    });

    const url = `${environment.baseUrl}${milestoneUrl}`;

    httpMock
      .expectOne((req: HttpRequest<any>) => {
        const body: Milestone = req.body;
        return (
          req.url === url && req.method === 'POST' && body === dummyMilestone
        );
      }, 'POST to api/milestones with milestone data in json format')
      .flush(responseMilestone, { status: 201, statusText: 'Created' });

    httpMock.verify();
  });

  it('should be able to handle rejection from server', () => {
    const dummyMilestone: Milestone = {
      id: 1,
      title: 'milestone title',
      description: 'milestone description',
      dueDate: '15/09/2018',
      repositoryId: 1
    };



    service.createMilestone(dummyMilestone).subscribe(
      repo => {
        expect(repo).toBeFalsy();
      },
      err => {
        expect(err).toBeTruthy();
      }
    );

    const url = `${environment.baseUrl}${milestoneUrl}`;

    httpMock
      .expectOne((req: HttpRequest<any>) => {
        const body: Milestone = req.body;
        return (
          req.url === url && req.method === 'POST' && body === dummyMilestone
        );
      }, 'POST to api/milestones with milestone data in json format')
      .flush(null, { status: 400, statusText: 'BadRequest' });

    httpMock.verify();
  });
});
