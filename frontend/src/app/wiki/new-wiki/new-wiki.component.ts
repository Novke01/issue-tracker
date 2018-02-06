import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { MatSnackBar } from '@angular/material';
import { ActivatedRoute, Router } from '@angular/router';

import { WikiPageService } from '../shared/wiki-page.service';
import { WikiPageSave } from './../shared/wiki-page-save.model';

@Component({
  selector: 'it-new-wiki',
  templateUrl: './new-wiki.component.html',
  styleUrls: ['./new-wiki.component.css']
})
export class NewWikiComponent implements OnInit {
  wikiPage: WikiPageSave = new WikiPageSave('', '', -1);
  form: FormGroup;
  repoId: number;

  constructor(
    private wikiPageService: WikiPageService,
    private route: ActivatedRoute,
    private formBuilder: FormBuilder,
    private snackBar: MatSnackBar,
    private router: Router
  ) {}

  ngOnInit() {
    this.form = this.formBuilder.group({
      name: ['', Validators.required],
      content: ['', Validators.required]
    });
  }

  create(wikiPage: WikiPageSave) {
    if (wikiPage.name === '' || wikiPage.content === '') {
      this.snackBar.open('Please enter name and content!', 'OK', {
        duration: 2000
      });
      return;
    }
    this.route.params.subscribe(params => {
      this.repoId = +params['id'];
      const newWikiPage = new WikiPageSave(
        wikiPage.name,
        wikiPage.content,
        this.repoId
      );
      this.wikiPageService.saveWikiPage(newWikiPage).subscribe(
        result => {
          this.wikiPage = new WikiPageSave('', '', -1);
          this.snackBar.open('You create a new wiki page!', 'OK', {
            duration: 2000
          });
          this.navigate();
        },
        err => {
          this.snackBar.open(err.message, 'Cancel', {
            duration: 2000
          });
        }
      );
    });
  }

  navigate() {
    this.router.navigate(['/repository', this.repoId]);
  }
}
