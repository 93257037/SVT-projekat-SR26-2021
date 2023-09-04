import { Component } from '@angular/core';

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.css']
})
export class LoginComponent {
  username: string = '';
  password: string = '';

  onSubmit() {
    // Add your login logic here
    if (this.username === 'your_username' && this.password === 'your_password') {
      // Successful login, you can navigate to another page or perform other actions
      console.log('Login successful');
    } else {
      // Invalid credentials, show an error message or handle it accordingly
      console.log('Login failed');
    }
  }
}
