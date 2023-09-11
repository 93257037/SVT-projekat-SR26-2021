import { Injectable } from '@angular/core';
import { Observable, throwError } from 'rxjs';
import { catchError, map } from 'rxjs/operators';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { JwtUtilsService } from './jwt-utils.service';

@Injectable()
export class AuthenticationService {

  private readonly loginPath = '/api/users/authenticate';

  constructor(private http: HttpClient, private jwtUtilsService: JwtUtilsService) { }

  login(username: string, password: string): Observable<boolean> {
    const headers: HttpHeaders = new HttpHeaders({ 'Content-Type': 'application/json' });
    return this.http.post('http://localhost:8080/api/users/login',{username: username, password: password })
      .pipe(
        map((response: any) => {
          let token = response.accessToken;
          if (token) {
            localStorage.setItem('currentUser', JSON.stringify({
              username: username,
              roles: this.jwtUtilsService.getRoles(token),
              token: token.split(' ')[1]
            }));
            return true;
          } else {
            return false;
          }
        }),
        catchError((error) => {
          console.error('Error during login:', error);
          return throwError(error);
        })
      );
  }

  getToken(): string | null {
    const currentUser = localStorage.getItem('currentUser');
    if (currentUser) {
      const { token } = JSON.parse(currentUser);
      return token ? token : null;
    }
    return null;
  }

  logout(): void {
    localStorage.removeItem('currentUser');
  }

  isLoggedIn(): boolean {
    return !!this.getToken();
  }

  getCurrentUser() {
    const currentUser = localStorage.getItem('currentUser');
    return currentUser ? JSON.parse(currentUser) : undefined;
  }

  hasToken() {
    return !!localStorage.getItem('currentUser');
  }

  isAdmin(): boolean {
    const currentUser = this.getCurrentUser();
    return currentUser && currentUser.username.includes('admin');
  }
}