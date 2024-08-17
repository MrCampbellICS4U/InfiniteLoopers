;; ethan's emacs config

(("src" .
  ((nil . ((eval .
		 (set-command (progn
				(async-shell-command "cd ${PWD%/InfiniteLoopers*}/InfiniteLoopers && make server" "server")
				(async-shell-command "cd ${PWD%/InfiniteLoopers*}/InfiniteLoopers && make client" "client")))))))))
