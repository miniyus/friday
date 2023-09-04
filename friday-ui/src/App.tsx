// src/App.tsx
import React from 'react';
import { BrowserRouter as Router, Route, Routes } from 'react-router-dom';
import Navbar from './components/layouts/NavBar';
import Home from './pages/Home';
import About from './pages/About';
import SideMenu from './components/layouts/SideMenu'; // SideMenu import 추가
import { createTheme, ThemeProvider } from '@mui/material/styles';
import Box from '@mui/material/Box';
import Toolbar from '@mui/material/Toolbar';
import Container from '@mui/material/Container';
import Host from './pages/hosts';
// TODO remove, this demo shouldn't need to reset the theme.
const defaultTheme = createTheme();

const App: React.FC = () => {
  const [open, setOpen] = React.useState(true);
  const toggleDrawer = () => {
    setOpen(!open);
  };

  return (
    <ThemeProvider theme={defaultTheme}>
      <Box sx={{ display: 'flex' }}>
        <Router>
          <Navbar open={open} toggleDrawer={toggleDrawer} title={'FriDay: UI'} />
          <SideMenu open={open} toggleDrawer={toggleDrawer} /> {/* SideMenu 추가 */}
          <Box
            component="main"
            sx={{
              backgroundColor: (theme) =>
                theme.palette.mode === 'light'
                  ? theme.palette.grey[100]
                  : theme.palette.grey[900],
              flexGrow: 1,
              height: '100vh',
              overflow: 'auto',
            }}
          >
            <Toolbar />
            <Container maxWidth="lg" sx={{ mt: 4, mb: 4 }}>
              <Routes>
                <Route path="/" element={<Home />} />
                <Route path="/about" element={<About />} />
                <Route path="/hosts" element={<Host />} />
              </Routes>
            </Container>
          </Box>
        </Router>
      </Box>
    </ThemeProvider>
  );
};

export default App;
