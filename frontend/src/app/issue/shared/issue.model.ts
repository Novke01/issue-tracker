export class Issue {
  id: number;
  repositoryId: number;
  title: string;
  description: string;
  created: number;
  ownerId: number;
  status: string;
  assignees: Array<number>;
  labels: Array<number>;
}
