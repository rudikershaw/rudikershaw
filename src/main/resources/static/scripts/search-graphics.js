import $ from 'jquery';

$(document).ready(function(){
    $('.container input[name="e-search-go"]').click(function(){
        const button = $(this);
        const searchFor = $('.container input[name="e-search-for"]').val();
        button.parent().find('li').removeClass('checked found');
        setTimeout(function(){
            button.parent().find('li').each(function(index, element){
                if($(element).text() == searchFor){
                    setTimeout(function(){
                        $(element).addClass('found');
                    }, 500*index);
                    throw "Found";
                } else {
                    setTimeout(function(){
                        $(element).addClass('checked');
                    }, 500*index);
                }

            });
        },500);
    });

    $('.container input[name="b-search-go"]').click(function(){
        const button = $(this);
        const find = $('.container input[name="b-search-for"]').val();
        button.parent().find('li').removeClass('checked found');
        setTimeout(function(){
            const lis = button.parent().find('li');
            let bottom = 0;
            let top = 13;
            let time = 0;
            while(bottom <= top)
            {
                const middle = (bottom + top) / 2 | 0;
                if($(lis.get(middle)).text() == find) {
                    setTimeout(function(){
                        $(lis.get(middle)).addClass('found');
                    },500*time);
                    throw "Found";
                } else if(parseInt($(lis.get(middle)).text()) > find) {
                    (function(middle){
                    setTimeout(function(){
                        $(lis.get(middle)).addClass('checked');
                    },500*time)}(middle));
                    top = middle - 1;
                } else if(parseInt($(lis.get(middle)).text()) < find) {
                    (function(middle){
                    setTimeout(function(){
                        $(lis.get(middle)).addClass('checked');
                    },500*time)}(middle));
                    bottom = middle + 1;
                }
                time++;
            }
        },500);
    });

    $('.container input[name="d-search-go"]').click(function(){
        const find = $('.container input[name="d-search-for"]').val();
        $('#depth-graph circle').attr('fill', '#333333').removeAttr('stroke stroke-width');
        $('#depth-graph circle.root').attr('stroke', 'black').attr('stroke-width', '4');
        setTimeout(function(){
            const root = new GraphFactory().getRoot();
            const stack = [];
            const visited = [];
            let time = 0;
            if(root.value == find){
                $('#depth-graph circle.root').attr('fill', '#ED5900');
                throw "Found";
            }
            visited.push(root);
            $('#depth-graph circle.root').attr('fill', '#777777');
            stack.push(root);
            while(stack.length > 0){
                const node = stack.pop();
                const nodename = node.name;
                (function(nodename){
                setTimeout(function(){
                    $('#depth-graph circle').removeAttr('stroke stroke-width');
                    $('#depth-graph circle.' + nodename).attr('stroke', 'black').attr('stroke-width', '4');
                },500*time)}(nodename));
                for(let i = 0; i < node.connections.length; i++){
                    time++;
                    const connectionname = node.connections[i].name;
                    if(node.connections[i].value == find){
                        setTimeout(function(){
                            $('#depth-graph circle.' + node.connections[i].name).attr('fill', '#ED5900');
                        },500*time);
                        throw "Found";
                    } else if(visited.indexOf(node.connections[i]) < 0){
                        (function(connectionname){
                        setTimeout(function(){
                            $('#depth-graph circle.' + connectionname).attr('fill', '#777777');
                        },500*time)}(connectionname));
                        stack.push(node.connections[i]);
                        visited.push(node.connections[i]);
                    }
                }
                time++;
            }
        },500);
    });

    $('.container input[name="bf-search-go"]').click(function(){
        const button = $(this);
        const find = $('.container input[name="bf-search-for"]').val();
        $('#breadth-graph circle').attr('fill', '#333333').removeAttr('stroke stroke-width');
            $('#breadth-graph circle.root').attr('stroke', 'black').attr('stroke-width', '4');
            setTimeout(function(){
                const root = new GraphFactory().getRoot();
                const queue = [];
                const visited = [];
                let time = 0;
                if(root.value == find){
                    $('#breadth-graph circle.root').attr('fill', '#ED5900');
                    throw "Found";
                }
                visited.push(root);
                $('#breadth-graph circle.root').attr('fill', '#777777');
                queue.push(root);
                while(queue.length > 0){
                    const node = queue.shift();
                    const nodename = node.name;
                    (function(nodename){
                    setTimeout(function(){
                        $('#breadth-graph circle').removeAttr('stroke stroke-width');
                        $('#breadth-graph circle.' + nodename).attr('stroke', 'black').attr('stroke-width', '4');
                    },500*time)}(nodename));
                    for(let i = 0; i < node.connections.length; i++){
                        time++;
                        const connectionname = node.connections[i].name;
                        if(node.connections[i].value == find){
                            setTimeout(function(){
                                $('#breadth-graph circle.' + node.connections[i].name).attr('fill', '#ED5900');
                            },500*time);
                            throw "Found";
                        } else if(visited.indexOf(node.connections[i]) < 0){
                            (function(connectionname){
                                setTimeout(function(){
                                    $('#breadth-graph circle.' + connectionname).attr('fill', '#777777');
                                },500*time)}(connectionname));
                                queue.push(node.connections[i]);
                                visited.push(node.connections[i]);
                        }
                    }
                    time++;
                }
            },500);
        });
});

// Set up graph structure for DFS and BFS examples.
function Node(value, name) {
    this.value = value;
    this.name = name;
    this.connections = [];
}

function GraphFactory() {
    this.getRoot = function(){
        const root = new Node(1, "root");
        const node2 = new Node(2, "node2");
        const node3 = new Node(3, "node3");
        const node4 = new Node(4, "node4");
        const node5 = new Node(5, "node5");
        const node6 = new Node(6, "node6");
        const node7 = new Node(7, "node7");
        const node8 = new Node(8, "node8");
        const node9 = new Node(9, "node9");
        const node10 = new Node(10, "node10");
        const node11 = new Node(11, "node11");
        const node12 = new Node(12, "node12");
        const node13 = new Node(13, "node13");
        const node14 = new Node(14, "node14");
        root.connections.push(node3);
        root.connections.push(node4);
        node3.connections.push(node2);
        node3.connections.push(node5);
        node3.connections.push(node6);
        node4.connections.push(node6);
        node4.connections.push(node7);
        node2.connections.push(node5);
        node5.connections.push(node8);
        node7.connections.push(node9);
        node7.connections.push(node10);
        node8.connections.push(node11);
        node8.connections.push(node12);
        node9.connections.push(node13);
        node9.connections.push(node14);
        node14.connections.push(node13);
        return root;
    }
}
