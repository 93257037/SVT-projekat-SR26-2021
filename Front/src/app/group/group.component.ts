import { HttpClient } from '@angular/common/http';
import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { AuthenticationService } from '../services/authentication-service.service';

@Component({
  selector: 'app-group',
  templateUrl: './group.component.html',
  styleUrls: ['./group.component.css']
})
export class GroupComponent implements OnInit{
  group = {
    name: '',
    description: ''
  }
  submitted: boolean = false;
  loading: boolean = false;

  constructor(
    private authService: AuthenticationService,
    private http: HttpClient, 
    private router: Router 
  ) { }

  ngOnInit() {
    // Provera da li je korisnik ulogovan
    if (!this.authService.hasToken()) {
      // Korisnik nije ulogovan, preusmeri ga na stranicu "home"
      this.router.navigate(['/']);
      return; // Prekini dalje izvršavanje metode
    }

  }



  logout() {
    localStorage.removeItem('currentUser');
    this.router.navigate(['/']);
  }
  
  // onSubmit() {

  //   this.loading = true;

  //   this.http.post('http://localhost:8080/api/groups/create', this.group).subscribe(
  //     (response) => {
  //       // Handle success response
  //       console.log('Group created successfully');
  //       this.loading = false;
  //       this.router.navigate(['/groups']);
  //     },
  //     (error) => {
  //       // Handle error response
  //       console.error('Error creating group:', error);
  //       this.loading = false;
  //     }
  //   );
  // }

  onSubmit() {
    this.loading = true;
  
    // Get the username of the logged-in user
    const currentUserString = localStorage.getItem('currentUser');
    if (currentUserString) {
      const currentUser = JSON.parse(currentUserString);
      if (currentUser && currentUser.username) {
        const username = currentUser.username;
  
        // Create the group object
        const groupData = {
          name: this.group.name,
          description: this.group.description
        };
  
        // Send the HTTP POST request to create the group
        this.http.post<any>(`http://localhost:8080/api/groups/create/${username}`, groupData)
          .subscribe(
            (response) => {
              // Handle success response
              console.log('Group created successfully');
              this.loading = false;
              this.router.navigate(['/groups']);
            },
            (error) => {
              // Handle error response
              console.error('Error creating group:', error);
              this.loading = false;
              this.router.navigate(['/groups']);

            }
          );
      } else {
        console.error('Error getting current user username');
        this.loading = false;
      }
    } else {
      console.error('No current user found');
      this.loading = false;
    }
  }
}