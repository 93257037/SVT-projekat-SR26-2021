import { Component } from '@angular/core';
import { Router } from '@angular/router';
import { HttpClient } from '@angular/common/http';
import { tap } from 'rxjs/operators';

@Component({
  selector: 'app-registration',
  templateUrl: './registration.component.html',
  styleUrls: ['./registration.component.css']
})
export class RegistrationComponent {
  registration = {
    firstName: '',
    lastName: '',
    email: '',
    username: '',
    password: ''
  };

  constructor(private http: HttpClient, private router: Router) { }

  onSubmit() {
    this.http.post('http://localhost:8080/api/users/register', this.registration)
    .subscribe(
      response => {
        // Registration successful, handle the response as needed
        console.log(response);
        // Reset the form
          this.registration = {
            firstName: '',
            lastName: '',
            email: '',
            username: '',
            password: ''
          };
          // Redirect to the home page or desired route
          this.router.navigate(['/login']);
        },
      error => {
        // Registration failed, handle the error
        console.error(error);
        // Handle error feedback to the user if needed
      }
    );
}

openLogin() {
  this.router.navigate(['/login']);
}


}
