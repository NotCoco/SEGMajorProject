<template>
	<section class="section">
		<link href="//netdna.bootstrapcdn.com/font-awesome/4.7.0/css/font-awesome.min.css" rel="stylesheet">
		<div style="overflow: hidden" class="custom-content-container" >
			<h1 class="title">Password Reset</h1>
			<div class="notification is-danger" v-if="loginError">
			<strong>Error:</strong> Please re-enter your credentials and try again.
			</div>
				<table style="border-collapse:separate; border-spacing:0px 14px;" >
					<tr><b>Enter your old password:</b><br></tr>
					<tr>
						<td>
							<input class="input" type="password" v-model="oldPw" v-show="!showoldPw" style="height: 30px;width: 200px;" placeholder="Enter old password">
							<input class="input" type="text"  v-model="oldPw" v-show="showoldPw" style="height: 30px;width: 200px;" placeholder="Enter old password">
							&nbsp;
							<button class="button" style="height: 30px;width: 15px;"  @click="showold()" id = "saveButton" v-show="!showoldPw" ><i class="fa fa-eye"></i></button>
							<button class="button" style="height: 30px;width: 15px;"  @click="showold()" id = "saveButton" v-show="showoldPw" ><i class="fa fa-eye-slash"></i></button>
						</td>
					</tr>
					<tr><b>Enter your new password:</b><br></tr>
					<tr>
						<td>
						<input class="input" type="password" v-model="newNw" v-show="!showNew" style="height: 30px;width: 200px;" placeholder="Enter new password"> 
						<input class="input" type="text" v-model="newNw" v-show="showNew" style="height: 30px;width: 200px;" placeholder="Enter new password"> 
						&nbsp;
						<button class="button"   style="height: 30px;width: 15px;"  @click="shownew()" id = "saveButton" v-show="!showNew" ><i class="fa fa-eye"></i></button>
						<button class="button"  style="height: 30px;width: 15px;"  @click="shownew()" id = "saveButton" v-show="showNew" ><i class="fa fa-eye-slash"></i></button>
						</td>
					</tr>
					<tr><b>Enter your new password again:</b><br></tr>
					<tr><input class="input" type="password" style="height: 30px;width: 200px;" placeholder="Enter again"></tr>
					<tr><button class="button"  style="height: 40px;width: 240px;" @click="resetPw()" id = "saveButton">Save</button></tr>
				</table>
		</div>
	</section>
</template>

<script>
	export default{
		data: function(){
			return{
				password: '',
				showoldPw: false,
				showNew: false
			}
			
		},
		methods: {
			showold: function(){
				this.showoldPw = !this.showoldPw
			},
			shownew: function(){
				this.showNew = !this.showNew
			},
			resetPw: function(){
			
			}
		}
	}
</script>

<style>
	.custom-content-container {
		display: flex;
		flex-direction: column;
		flex-grow: 1;
		justify-content: center;
	
		width: 300%;
		max-width: 600px;
	}
</style>
