import dashboardBlack from './icons/dashboard-black.svg';
import profileBlack from './icons/profile-black.svg';
import settingsBlack from './icons/settings-black.svg';
import usersBlack from './icons/users-black.svg';
import logoutBlack from './icons/logout-black.svg';
import adminsBlack from './icons/admin-black.svg';
import defaultBlack from './icons/default-black.svg';
import rolesBlack from './icons/roles-black.svg';

const sidebarLinks = [
    { id: 1, name: 'Dashboard', link: '/dashboard' , icon: dashboardBlack},
    { id: 2, name: 'Profile', link: '/profile' , icon: profileBlack},
    { id: 3, name: 'Settings', link: '/settings', icon: settingsBlack, multy: true, 
        subLinks: [
            { id: 1, name: 'Admins', link: '/admins', icon: adminsBlack },
            { id: 2, name: 'Users', link: '/users', icon: usersBlack },
            { id: 3, name: 'Roles', link: '/roles', icon: rolesBlack },
        ]
    },
    { id: 4, name: 'Help', link: '/help', icon: defaultBlack },
    { id: 5, name: 'About', link: '/about', icon: defaultBlack },
    { id: 6, name: 'Logout', link: '/logout', icon: logoutBlack },
    { id: 15, name: 'Users', link: '/users', icon: usersBlack, multy: true, 
        subLinks: [
            { id: 1, name: 'Admins', link: '/admins', icon: adminsBlack },
            { id: 2, name: 'Users', link: '/users', icon: usersBlack },
        ]
     },
    { id: 7, name: 'Dashboard', link: '/dashboard' , icon: defaultBlack, multy: true,
        subLinks: [
            { id: 1, name: 'Admins', link: '/admins', icon: defaultBlack },
            { id: 2, name: 'Users', link: '/users', icon: defaultBlack },
        ]},
    { id: 8, name: 'Profile', link: '/profile' , icon: defaultBlack},
    { id: 9, name: 'Settings', link: '/settings', icon: defaultBlack },
    { id: 10, name: 'Help', link: '/help', icon: defaultBlack },
    { id: 11, name: 'About', link: '/about', icon: defaultBlack },
    { id: 12, name: 'Logout', link: '/logout', icon: defaultBlack },
    { id: 13, name: 'Logout', link: '/logout', icon: defaultBlack },
    { id: 14, name: 'Logout', link: '/logout', icon: defaultBlack },
];

export default sidebarLinks;