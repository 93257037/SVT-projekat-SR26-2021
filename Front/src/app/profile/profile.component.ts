import { Component, OnInit } from '@angular/core';
import { AuthenticationService } from '../services/authentication-service.service';
import { ActivatedRoute } from '@angular/router';
import { HttpClient } from '@angular/common/http';
import { Router } from '@angular/router';

@Component({
  selector: 'app-profile',
  templateUrl: './profile.component.html',
  styleUrls: ['./profile.component.css']
})
export class ProfileComponent  implements OnInit {
  passwordData = {
    currentPassword: '',
    newPassword: '',
    confirmPassword: ''
  };
  
  currentUser: any;
  groups: any[] = []; // Initialize the "groups" property with an empty array


  constructor( 
    private router: Router, 
    private authService: AuthenticationService, 
    private route: ActivatedRoute, 
    private http: HttpClient
    ) {}

  ngOnInit() {
    if (!this.authService.hasToken()) {
      this.router.navigate(['/']);
      return; 
    }
    this.currentUser = this.authService.getCurrentUser(); // Dohvati trenutnog korisnika
    this.getUserGroups(this.currentUser.username); // Dohvati grupe korisnika

  }

  logout() {
    localStorage.removeItem('currentUser');
    this.router.navigate(['/']);
  }

  onSubmit() {
    const currentUser = this.authService.getCurrentUser();
    if (currentUser) {
      // User is logged in, perform actions
      console.log(currentUser.username);
      console.log(currentUser.roles);
    } else {
      // User is not logged in, handle accordingly
      console.log('User is not logged in');
    }
    // Check if the new password and confirm password match
    if (this.passwordData.newPassword !== this.passwordData.confirmPassword) {
      // Handle password mismatch error
      console.error('New password and confirm password do not match');
      return;
    }
    // Make a POST request to the backend with the password change data
    this.http.put('http://localhost:8080/api/users/changePassword/'+ currentUser.username, this.passwordData.newPassword, { responseType: 'text' }).subscribe(
      (response: any) => {
        localStorage.removeItem('token');
        this.router.navigate(['/login']);
      }, (error) => {
        console.error('change pass failed:', error);
      })
      
    
    // Include any additional logic or validations here

    // Reset the form
    this.passwordData = {
      currentPassword: '',
      newPassword: '',
      confirmPassword: ''
    };
  }

  getUserGroups(username: string) {
    const url = `http://localhost:8080/api/groups/groups/${username}`;

    this.http.get(url).subscribe(
      (response: any) => {
        this.groups = response; // Spremi dobavljene grupe u varijablu "groups"
      },
      (error) => {
        console.error('Dohvaćanje grupa nije uspjelo:', error);
      }
    );
  }
}
