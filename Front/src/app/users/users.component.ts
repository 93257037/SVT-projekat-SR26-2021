import { HttpClient } from '@angular/common/http';
import { Component } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { AuthenticationService } from '../services/authentication-service.service';


@Component({
  selector: 'app-users',
  templateUrl: './users.component.html',
  styleUrls: ['./users.component.css']
})
export class UsersComponent {
  users: any[] = [];

  constructor(
    private authService: AuthenticationService,
    private http: HttpClient,
    private router: Router) {}

  ngOnInit() {
    if (!this.authService.hasToken()) {
      this.router.navigate(['/']);
      return; 
    }
    // if(!this.authService.isAdmin()){
    //   this.router.navigate(['/']);
    //   return; 
    // }
    this.loadAllUsers();
  }

  logout() {
    localStorage.removeItem('currentUser');
    this.router.navigate(['/']);
  }

  loadAllUsers() {
    this.http.get<any[]>('http://localhost:8080/api/users/all').subscribe(
      (users) => {
        this.users = users;
      },
      (error) => {
        console.error(error);
      }
    );
  }

  // isAdmin(): boolean {
  //   const currentUser = this.authService.getCurrentUser();
  //   return currentUser && currentUser.role === 'ADMIN';
  // }
}