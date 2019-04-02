# Simple Explanation of Github Workflow

For those who never use Github or just if You feel that You need to read this!

__FYI__ I do it by terminal because We will use diffrent IDE to diffrent things and I don't want this to be like a 50 pages guide. All commands should work on Windows and some Linux distro. Maybe `tree` command will be process different, but I use it just to showcase something.

### Projects tab and naming conventions

![nic](https://i.imgur.com/ivBJdHt.gif)

### Create branch, switch to it and hen push it!

![nic](https://i.imgur.com/qzJWUXP.gif)

__Commands used:__

```
git checkout -b all/app_design
git push origin all/app_design
```

The `-b` near `git checkout` creates new branch. If You want just to switch to branch You can just `git checkout branch_name`

### Do Your stuff and then add, commit and push!

![nic](https://i.imgur.com/GCXhmYR.gif)

__Commands used:__

```
git add --all
git commit -am 'Some info'
git push origin all/app_design
```

### Just a reminder: Difference between Your branch and master

![nic](https://i.imgur.com/IWRrtb2.gif)

### Create pull request via github page: switch to your branch and create pull request!

![nic](https://i.imgur.com/ZIegmv7.gif)

### Now You can git pull master to get your files!

![nic](https://i.imgur.com/YOKLk0I.gif)