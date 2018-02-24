export class WikiPageSave {
  id: number;
  name: string;
  content: string;
  repositoryId: number;

  constructor(id: number, name: string, content: string, repositoryId: number) {
    this.id = id;
    this.name = name;
    this.content = content;
    this.repositoryId = repositoryId;
  }
}
