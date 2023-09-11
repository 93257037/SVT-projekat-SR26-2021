import { Component, OnInit } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Router } from '@angular/router';
import { AuthenticationService } from '../services/authentication-service.service';

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.css']
})
export class LoginComponent implements OnInit {
  username: string;
  password: string;

  constructor(private http: HttpClient, private router: Router, private authService: AuthenticationService) {
    this.username = '';
    this.password = '';
  }

  ngOnInit() {
    this.clearFields();
  }

  login() {
    this.authService.login(this.username, this.password).subscribe(
      (isLoggedIn) => {
        if (isLoggedIn) {
          // Korisnik je uspešno prijavljen, izvršava se redirekcija na stranicu "main"
          this.router.navigate(['/main']);
        } else {
          // Korisnik ne postoji u bazi, prikazuje se odgovarajuća greška
          window.alert('Korisnik ne postoji u bazi');
        }
      },
      (error) => {
        // Greška prilikom prijavljivanja
        window.alert('Greška prilikom prijavljivanja:');
      }
    );
  }

  clearFields() {
    this.username = '';
    this.password = '';
  }

  openRegistration() {
    this.router.navigate(['/registration']);
  }
}