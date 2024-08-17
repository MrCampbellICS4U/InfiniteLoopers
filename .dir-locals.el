;; ethan's emacs config

(("src" .
  ((nil . ((eval .
		 (progn (require 'dash)
			(set-command (progn
				       (let (kill-buffer-query-functions)
					 (-some-> (get-buffer "server") (kill-buffer))
					 (-some-> (get-buffer "server") (kill-buffer)))
				       (async-shell-command "cd ${PWD%/InfiniteLoopers*}/InfiniteLoopers && make server" "server")
				       (async-shell-command "cd ${PWD%/InfiniteLoopers*}/InfiniteLoopers && make client" "client"))))))))))
