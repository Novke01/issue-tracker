
export class WikiPageSave {
    name: string;
    content: string;
    repositoryId: number;

    constructor(name: string, content: string, repositoryId: number) {
      this.name = name;
      this.content = content;
      this.repositoryId = repositoryId;
    }
  }
