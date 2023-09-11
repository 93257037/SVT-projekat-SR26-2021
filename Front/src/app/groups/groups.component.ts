import { Component, OnInit } from '@angular/core';
import { Observable } from 'rxjs';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { ActivatedRoute, Router } from '@angular/router';
import { AuthenticationService } from '../services/authentication-service.service';

@Component({
  selector: 'app-groups',
  templateUrl: './groups.component.html',
  styleUrls: ['./groups.component.css']
})
export class GroupsComponent implements OnInit {
  groups: any[] = []; 
  loading: boolean = false;

  constructor(
    private authService: AuthenticationService,
    private http: HttpClient, 
    private router: Router) {}

  ngOnInit() {
    if (!this.authService.hasToken()) {
      this.router.navigate(['/']);
      return; 
    }
    this.fetchGroups();
  }

  currentUser: any;

  logout() {
    localStorage.removeItem('currentUser');
    this.router.navigate(['/']);
  }
  
  fetchGroups() {
    this.loading = true;
    this.http.get<any[]>('http://localhost:8080/api/groups').subscribe(
      (groups) => {
        // Assign the retrieved groups to a component property or perform any other necessary operations
        this.groups = groups;
        this.loading = false;
      },
      (error) => {
        // Handle the error
        console.error(error);
        this.loading = false;
      }
    );
  }
  
  editGroup(groupId: number) {
    const newName = prompt('Enter new group name:');
    const newContent = prompt('Enter new group content:');
  
    if ((newName !== null && newName !== '') || (newContent !== null && newContent !== '')) {
      const updatedGroup: any = {};
  
      if (newName !== null && newName !== '') {
        updatedGroup.name = newName;
      }
  
      if (newContent !== null && newContent !== '') {
        updatedGroup.description = newContent; // Use 'description' instead of 'content'
      }
  
      this.http.put(`http://localhost:8080/api/groups/update/${groupId}`, updatedGroup).subscribe(
        (response) => {
          // console.log(response);
          this.fetchGroups();
        },
        (error) => {
          // console.error(error);
          this.fetchGroups();

        }
      );
    }
  }

  isAdmin(): boolean {
    return this.authService.isAdmin();
  }

  // isGroupAdmin(groupId: number): boolean {
  //   const currentUser = this.authService.getCurrentUser();
  //   const group = this.groups.find((g) => g.id === groupId);
  
  //   return group && group.admin === currentUser.username;
  // }

  isGroupAdmin(groupId: number): boolean {
    const currentUser = this.authService.getCurrentUser();
    const group = this.groups.find((g) => g.id === groupId);
  
    return group && group.groupAdmin === currentUser.username;
  }
  
  
  deleteGroup(groupId: number) {
    if (confirm('Are you sure you want to delete this group?')) {
      this.http.delete(`http://localhost:8080/api/groups/delete/${groupId}`).subscribe(
        (response) => {
          console.log(response);
          // Handle the successful deletion here, such as refreshing the group list
          this.fetchGroups();
        },
        (error) => {
          // console.error(error);
          this.fetchGroups();

          // Handle any error that occurred during deletion
        }
      );
    }
  }


}