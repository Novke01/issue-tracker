import { Component, OnInit } from '@angular/core';
import { UserService } from '../user/shared/user.service';

@Component({
  selector: 'it-home',
  templateUrl: './home.component.html',
  styleUrls: ['./home.component.css']
})
export class HomeComponent implements OnInit {

  message: String;

  constructor(private userService: UserService) { }

  ngOnInit() {
    this.userService.getHome().subscribe(
      obj => this.message = obj['text'],
      err => this.message = ''
    );
  }

}
