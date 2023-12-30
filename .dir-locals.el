;; ethan's emacs config

((nil . ((eval .
	       (progn
		 (defun run-client ()
		   (split-window-vertically)
		   (other-window 1)
		   (term "/bin/bash")
		   (rename-buffer "term-client" t)
		   (execute-kbd-macro "make client\n"))
		 (defun run-term ()
		   (interactive)
		   (split-window-horizontally)
		   (other-window 1)
		   (term "/bin/bash")
		   (rename-buffer "term-server")
		   (execute-kbd-macro "make server\n")
		   (sleep-for 0 300)

		   (run-client)
		   (run-client)
		   (other-window -3))
		 (local-keybind-mode 1)
		 (evil-define-key 'normal local-keybind-mode-map (kbd "C-;") #'run-term))))))
