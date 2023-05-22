# FTP Client

The FTP Client is a Java application that provides functionality for interacting with FTP (File Transfer Protocol) servers. It allows users to connect to remote FTP servers, browse directories, upload and download files, and perform various file management operations.

## Features

- **Connect to FTP Server**: Establish a connection with a remote FTP server by providing the hostname, port number, username, and password.
- **Browse Remote Directories**: Navigate through the directory structure of the connected FTP server.
- **Browse Local Directories**: Navigate through the directory structure of the local computer.
- **Upload Files**: Transfer local files to the remote FTP server.
- **Download Files**: Retrieve files from the remote FTP server and save them locally.
- **File Management**: Create directories on the remote FTP server.
- **Binary Mode**: Support for transferring files in binary mode
- **Error Handling**: Handle various error conditions gracefully, providing meaningful error messages to the user.

![ScreenShot](demo/screenshots/main.jpg?raw=true "Main window")

## Prerequisites

- Java Development Kit (JDK) 8 or later

## Usage

1. Clone the repository.

2. Open the project in your preferred Java IDE.

3. Build the project to compile the Java source code.

4. Run the FTPClientGUI class to start the FTP client application.

5. Enter the FTP server details (hostname, port, username, and password) to establish a connection.

6. Use the provided UI buttons to perform desired FTP operations (e.g., navigating directories, uploading/downloading files).

7. Close the application to disconnect from the FTP server.

## Dependencies

The FTP client relies on the following external libraries:

- [Apache Commons Net](https://commons.apache.org/proper/commons-net/): A library for working with various Internet protocols, including FTP.
- [Java Swing](https://docs.oracle.com/javase%2F7%2Fdocs%2Fapi%2F%2F/javax/swing/package-summary.html): Library for the UI.
- [H2 Database Engine](https://www.h2database.com/html/main.html): A database engine to store user connections.

## Contributing

Contributions are welcome! If you find any issues or have suggestions for improvements, please open an issue or submit a pull request to the repository.

## License

This project is licensed under the [MIT License](LICENSE).

## Acknowledgments

- [Apache Commons Net](https://commons.apache.org/proper/commons-net/) for providing the FTP client library.
- [Pure-FTPD](https://www.pureftpd.org/project/pure-ftpd/) for testing the FTP client application.
