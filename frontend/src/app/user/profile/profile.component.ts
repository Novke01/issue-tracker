import { Component, OnInit } from '@angular/core';
import { User } from '../../core/auth/user.model';
import { ActivatedRoute } from '@angular/router';
import { UserService } from '../shared/user.service';

@Component({
  selector: 'it-profile',
  templateUrl: './profile.component.html',
  styleUrls: ['./profile.component.css']
})
export class ProfileComponent implements OnInit {

  user: User;

  constructor(
    private userService: UserService,
    private router: ActivatedRoute
  ) { }

  ngOnInit(): void {
    this.router.params.subscribe(params =>
      this.userService.getUserData(+params['id']).subscribe(user => this.user = user)
    );
  }

}
