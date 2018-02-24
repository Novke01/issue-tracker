import 'rxjs/add/operator/switchMap';

import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, ParamMap } from '@angular/router';

import { WikiPageService } from '../shared/wiki-page.service';
import { WikiPage } from './../shared/wiki-page.model';

@Component({
  selector: 'it-repository-wiki',
  templateUrl: './repository-wiki.component.html',
  styleUrls: ['./repository-wiki.component.css']
})
export class RepositoryWikiComponent implements OnInit {
  wikiPages: WikiPage[];
  currentWiki: WikiPage = new WikiPage();
  selectedPage: number;

  constructor(
    private wikiPageService: WikiPageService,
    private route: ActivatedRoute
  ) {}

  ngOnInit() {
    this.wikiPages = [];
    this.route.paramMap
      .switchMap((params: ParamMap) =>
        this.wikiPageService.getWikiPageByRepositoryId(params.get('id'))
      )
      .subscribe(result => {
        this.wikiPages = result;
        if (this.wikiPages.length > 0) {
          this.selectedPage = this.wikiPages[0].id;
          this.currentWiki = this.wikiPages[0];
        }
      });
  }

  onChanged() {
    this.currentWiki = this.wikiPages.filter(
      x => x.id === this.selectedPage
    )[0];
  }

  remove(id: number) {
    this.wikiPageService.remove(id).subscribe(() => {
      this.wikiPages = this.wikiPages.filter(function(l) {
        return l.id !== id;
      });
      if (this.wikiPages.length > 0) {
        this.selectedPage = this.wikiPages[0].id;
        this.currentWiki = this.wikiPages[0];
      } else if (this.wikiPages.length === 0) {
        this.currentWiki = new WikiPage();
        this.currentWiki.content = '';
      }
    });
  }
}
