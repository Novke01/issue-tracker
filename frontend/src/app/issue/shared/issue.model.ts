export class Issue {
    id: number;
    title: string;
    description: string;
    created: number;
    ownerId: number;
    status: string;
    assignees: Array<number>
}
