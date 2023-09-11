import { Component, OnInit } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { ActivatedRoute, Router } from '@angular/router';
import { AuthenticationService } from '../services/authentication-service.service';

@Component({
  selector: 'app-main',
  templateUrl: './main.component.html',
  styleUrls: ['./main.component.css']
})
export class MainComponent implements OnInit {
  registration = {
    groupId: -1,
    content: '',
    userName: ''
  };
  groups: any[] = [];
  posts: any[] = [];
  loading: boolean = false;
  currentUserId: number | null; 

  constructor(
    private authService: AuthenticationService,
    private route: ActivatedRoute,
    private http: HttpClient,
    private router: Router
  ) {
    this.currentUserId = null; // Initialize currentUserId in the constructor

  }

  ngOnInit() {
    if (!this.authService.hasToken()) {
      this.router.navigate(['/']);
      return; 
    }
    
    this.registration.userName = this.authService.getCurrentUser().username;
    this.getGroups();
    this.getPosts();
  }

  
  logout() {
    localStorage.removeItem('currentUser');
    this.router.navigate(['/']);
  }

  isAdmin(): boolean {
    return this.authService.isAdmin();
  }
  
  hasToken() {
    return this.authService.hasToken();
  }

  isOwner(postId: number): boolean {
    const currentUser = this.authService.getCurrentUser();
    const post = this.posts.find((p) => p.id === postId);
    return post && post.userName === currentUser.username;
  }

  getGroups() {
    this.http.get<any>('http://localhost:8080/api/groups').subscribe(
      (groups) => {
        this.groups = groups;
      },
      (error) => {
        // console.error(error);
      }
    );
  }

  getPosts() {
    this.loading = true;
    this.http.get<any>('http://localhost:8080/api/posts/all').subscribe(
      (posts) => {
        this.posts = posts;
        for (const post of this.posts) {
          this.fetchGroupName(post.groupId, post);
          this.getReactions(post.id); // Fetch reactions for each post
        }
        this.loading = false;
      },
      (error) => {
        // console.error(error);
        this.loading = false;
      }
    );
  }

  fetchGroupName(groupId: number, post: any) {
    this.loading = true;
    this.http.get<any[]>('http://localhost:8080/api/groups').subscribe(
      (groups) => {
        const foundGroup = groups.find(group => group.id === groupId);
        if (foundGroup) {
          const groupName = foundGroup.name;
          post.groupName = groupName; // Dodaj ime grupe u post objekat
        }
        this.loading = false;
      },
      (error) => {
        // console.error(error);
        this.loading = false;
      }
    );
  }
 
 

  createPost() {
    if (this.registration.content === '' || this.registration.groupId === -1) {
      alert('Please fill in all fields');
    } else {
      this.http.post('http://localhost:8080/api/posts/create', this.registration).subscribe(
        (response) => {
          // console.log(response);
          this.registration = {
            groupId: -1 ,
            content: '',
            userName: ''
          };
          this.getPosts();
        },
        (error) => {
          // console.error(error);
        }
      );
    }
  }

  editPost(postId: number) {
    const content = prompt('Enter new content:'); // Korisnik unosi novi sadržaj posta
  
    if (content !== null && content !== '') {
      const updatedPost = { content }; // Kreiranje objekta sa novim sadržajem posta
  
      this.http.put(`http://localhost:8080/api/posts/update/${postId}`, content).subscribe(
        (response) => {
          // console.log(response);
          // Ažurirajte prikaz postova ili osvežite listu postova
          this.getPosts();
        },
        (error) => {
          // console.error(error);
        }
      );
    }
  }

  deletePost(postId: number) {
    if (confirm('Are you sure you want to delete this post?')) {
      this.http.delete(`http://localhost:8080/api/posts/delete/${postId}`).subscribe(
        (response) => {
          // console.log(response);
          // Remove the post from the array or update the display of posts
          this.getPosts();
        },
        (error) => {
          // console.error(error);
        }
      );
    }
  }
  
createReaction(postId: number, reactionType: string) {
  const currentUser = this.authService.getCurrentUser();

  if (!currentUser) {
    console.error('User not logged in.');
    return;
  }

  const reaction = {
    postId: postId,
    reactionType: reactionType,
    userName: currentUser.username
  };

  this.http.post('http://localhost:8080/api/reactions/create', reaction).subscribe(
    (response) => {
      // console.log(response);
      // Perform any necessary actions after creating the reaction
      this.getReactions(postId); // Refresh the reactions for the specific post
    },
    (error) => {
      // console.error(error);
    }
  );
}
  
// refreshReactions(postId: number) {
//   // Osveži brojače reakcija za određeni post
//   const post = this.posts.find(p => p.id === postId);
//   if (post) {
//     this.getReactions(postId);
//   }
// }

getReactions(postId: number) {
  const url = `http://localhost:8080/api/reactions/all?postId=${postId}`;

  this.http.get<any>(url).subscribe(
    (reactions) => {
      // console.log(reactions);
      this.updateReactionCounters(postId, reactions);
    },
    (error) => {
      // console.error(error);
    }
  );
}
  
  updateReactionCounters(postId: number, reactions: any[]) {
    const post = this.posts.find((p) => p.id === postId);
    if (post) {
      // Resetuj brojače reakcija na nulu
      post.likeCounter = 0;
      post.dislikeCounter = 0;
      post.heartCounter = 0;
  
      reactions.forEach((reaction) => {
        if (reaction.postId === postId) {
          if (reaction.reactionType === 'LIKE') {
            post.likeCounter++;
          } else if (reaction.reactionType === 'DISLIKE') {
            post.dislikeCounter++;
          } else if (reaction.reactionType === 'HEART') {
            post.heartCounter++;
          }
        }
      });
    }
  }


  refreshReactions(postId: number) {
    this.getReactions(postId);
  }

//   getCurrentUserId(username: string) {
//     this.http.get<User[]>('http://localhost:8080/api/users/all').subscribe(
//       (users: User[]) => {
//         const currentUser = users.find(user => user.username === username);
//         this.currentUserId = currentUser ? currentUser.id : null;
//       },
//       (error) => {
//         console.error(error);
//       }
//     );
//   }

  // deleteReaction(postId: number, reactionId: number) {
  //   const url = `http://localhost:8080/api/reactions/delete/${reactionId}`;
  
  //   this.http.delete(url).subscribe(
  //     (response) => {
  //       console.log(response);
  //       // Perform any necessary actions after deleting the reaction
  //       this.getReactions(postId); // Refresh the reactions for the specific post
  //     },
  //     (error) => {
  //       console.error(error);
  //     }
  //   );
  // }




}

interface User {
  id: number;
  username: string;
}