import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';
import { FormsModule } from '@angular/forms';
import { HttpClientModule } from '@angular/common/http';


import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { HomeComponent } from './home/home.component';
import { LoginComponent } from './login/login.component';
import { MainComponent } from './main/main.component';
import { RegistrationComponent } from './registration/registration.component';
import { ProfileComponent } from './profile/profile.component';

import { AuthenticationService } from './services/authentication-service.service';
import { JwtUtilsService } from './services/jwt-utils.service';
import { GroupsComponent } from './groups/groups.component';
import { GroupComponent } from './group/group.component';
import { UsersComponent } from './users/users.component';

@NgModule({
  declarations: [
    AppComponent,
    HomeComponent,
    LoginComponent,
    MainComponent,
    RegistrationComponent,
    ProfileComponent,
    GroupsComponent,
    GroupComponent,
    UsersComponent
  ],
  imports: [
    BrowserModule,
    HttpClientModule,
    FormsModule,
    AppRoutingModule    
  ],
  providers: [AuthenticationService,JwtUtilsService],
  bootstrap: [AppComponent]
})
export class AppModule { }