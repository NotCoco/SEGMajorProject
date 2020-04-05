<template>
	
	<section class="section">
		<div style="overflow: hidden" class="custom-content-container" >
			<h1 class="title">Password Reset</h1>
			<!-- notifications -->
			<div class="notification is-success" v-show="Success">
			<strong>Success:</strong> Password has been reset, redirected to login page!&nbsp;<i class="fa fa-spinner fa-spin"></i>
			</div>
			<div class="notification is-danger" v-show="submitError">
			<strong>Error:</strong> Please fill up your info and try again!
			</div>
			<div class="notification is-danger" v-show="lengthError">
			<strong>Error:</strong> Password is too short!
			</div>
			<div class="notification is-danger" v-show="matchError">
			<strong>Error:</strong> Password not match!
			</div>
			<div class="notification is-danger" v-show="verifyError">
			<strong>Error:</strong> Verify Failed! Please check your token.
			</div>
				<table class="passwordtable-layout">
					<!-- password input box -->
					<tr><b>First, enter the new password:</b><br></tr>
					<tr>
						<td>
						<input id="newPw" class="input-layout input" type="password" v-model="newPw" v-show="!showNew"  placeholder="Enter new password"> 
						<input id="newPw" class="input-layout input" type="text" v-model="newPw" v-show="showNew" placeholder="Enter new password"> 
						&nbsp;
						<button class="button-layout button"  @click="shownew()" id = "saveButton" v-show="!showNew" ><i class="fa fa-eye"></i></button>
						<button class="button-layout button"  @click="shownew()" id = "saveButton" v-show="showNew" ><i class="fa fa-eye-slash"></i></button>
						</td>
					</tr>
					<!-- password input box -->
					<tr><b>Please enter it again:</b><br></tr>
					<tr><input id="newPw_2" class="input-layout input" type="password"  placeholder="Enter again"></tr>
					<!-- token input box -->
					<tr><b>Last step, enter the verify code in the email:</b><br></tr>
					<tr>
						<td>
						<input class="input-layout input" id="token" type="text" placeholder="Enter token">
						&nbsp;
						<button class="button"  v-bind:disabled="!canClick" style="height: 30px;width: 85px;"  @click="sendRequest()" id = "sendButton"  >
							<i v-show="canClick" class="fa fa-send-o">&nbsp;{{this.content}}</i>
							<i v-show="!canClick" class="fa fa-refresh fa-spin"></i><i v-show="!canClick">{{this.content}}</i>
						</button>
						</td>
					</tr>
					<tr><button class="button"  style="height: 40px;width: 240px;"  @click="resetPw()" id = "saveButton">Save</button></tr>
				</table>
		</div>
	</section>
</template>

<script>
	import userService from '../services/user-service.js'
	export default{
		data: function(){
			return{
				content: 'Send',
				time: 10,
				password: '',
				/**
				 *  notifications
				 */
				showNew: false,
				submitError: false,
				verifyError: false,
				matchError: false,
				lengthError: false,
				Success: false,
				canClick: true
				
			}
			
		},
		created() {
			/**
			 * Check if the countdown is still counting when refreshing pages
			 */
			var t = window.localStorage.getItem("time")
			var fac = this.time
			if((fac- t)>0){
				this.time = t
				this.send()
			}

		},
		watch:{
			/**
			 * save countdown
			 */
			time:{
				handler: function(){
					if(window.localStorage){
						window.localStorage.setItem("time",JSON.stringify(this.time));
					}else{
						console.log("failed")
					}
				}
			}
		},
		methods: {
			/**
			 * password hide/display
			 */
			shownew: function(){
				this.showNew = !this.showNew
			},
			/**
			 * control whether resend button can be clicked
			 */
			send: function(){
				this.canClick = false
				this.content = this.time + 's'
				let clock = window.setInterval(() => {
					this.time--
					this.content = this.time + 's'
					if (this.time < 0) {
						window.clearInterval(clock)
						this.content = 'Resend'
						this.time = 10
						this.canClick = true
					}
				},1000)
			},
			/**
			 * send the password reset request
			 */
			sendRequest: function(){
				var email =  window.localStorage.getItem("email")
				email = email.substr(0,email.length-1)
				email = email.substr(1)
				userService.getResetRequest(email)
				this.canClick = false
				this.content = this.time + 's'
				let clock = window.setInterval(() => {
					this.time--
					this.content = this.time + 's'
					if (this.time < 0) {
						window.clearInterval(clock)
						this.content = 'Resend'
						this.time = 10
						this.canClick = true
					}
				},1000)
			},
			/**
			 * reset the password
			 * control to display the notification
			 */
			async resetPw(){
				var status = 0
				var tk = document.getElementById('token').value
				var pw = document.getElementById('newPw').value
				var pw_len = pw.length
				var pw_2 = document.getElementById('newPw_2').value
				if(pw_len<5){
					this.lengthError = true
					this.submitError = false
					this.verifyError = false
					this.matchError = false
				}
				else if(pw !== "" && pw_2 !== "" && pw === pw_2 && tk!==""){
					await userService.resetPassword(tk,pw).then(async function(response) {
							console.log(response.data)
							status = 1
							userService.logout()
							location.reload()
						})
						.catch(function(error) {
							console.log(error)
							status = -1
						})
					if(status === -1){
						this.verifyError = true
					}
					if(status === 1){
						this.Success = true
					}
					this.lengthError = false
					this.matchError = false
					this.submitError = false
					
				}else if(pw === "" || pw_2 === "" || tk==="" ){
					this.submitError = true
					this.matchError = false
					this.verifyError = false
					this.lengthError = false
				}
				else if(pw !== pw_2){
					this.matchError = true
					this.submitError = false
					this.verifyError = false
					this.lengthError = false
				}
				
			}
		}
	}
</script>

<style lang="scss" scoped>
	.custom-content-container {
		display: flex;
		flex-direction: column;
		flex-grow: 1;
		justify-content: center;
	
		width: 300%;
		max-width: 600px;
	}
  .passwordtable-layout{
    border-collapse:separate; 
    border-spacing:0px 14px;
  }
  .input.input-layout{
    height: 30px;
    width: 200px;
  }
  .button.button-layout{
    height: 30px;
    width: 15px;
  }
</style>
